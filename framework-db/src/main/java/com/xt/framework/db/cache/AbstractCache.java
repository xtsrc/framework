package com.xt.framework.db.cache;

import cn.hutool.core.util.ObjectUtil;
import com.xt.framwork.common.core.util.GenericSuperclassUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.redisson.api.*;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.BeanUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Objects;

import static java.util.concurrent.Executors.newSingleThreadExecutor;

/**
 * 缓存的通用抽象类，解决了 雪崩、击穿、穿透的问题
 *
 * @author tao.xiong
 */
@Slf4j
public abstract class AbstractCache<T,V> {
    protected Class<?> vClass = GenericSuperclassUtil.getActualTypeArgument(getClass(),1);

    protected boolean isHotData=HotData.class.isAssignableFrom(vClass);
    @Resource
    private RedissonClient redissonClient;

    /**
     *
     * @return 临界资源唯一标识
     */
    public abstract String getKey();
    @PostConstruct
    public abstract void initBloomFilter();
    /**
     * 待缓存的数据，当前的方法需要重写
     *
     * @param param 参数
     * @return 返回 待缓存的值
     */
    public abstract V willCacheData(T param);

    /**
     * 限流
     * @param param 查询的参数
     * @return 返回 值
     */

    public V getValueRateLimiter(T param) {
        String rateLimiterKey = String.format(Constants.CACHE_PREFIX, String.format(Constants.CACHE_RATE_LIMITER,getKey()));
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(rateLimiterKey);
        if (!rateLimiter.isExists()) {
            // 设置 速率模式，速率，间隔，间隔单位
            rateLimiter.trySetRate(Constants.RATE_LIMITER_RATE_TYPE, Constants.RATE_LIMITER_RATE
                    , Constants.RATE_LIMITER_RATE_INTERVAL, Constants.RATE_LIMITER_RATE_INTERVAL_UNIT);
        }
        // 获取 访问许可rateLimiter acquire();
        if (!rateLimiter.tryAcquire(Constants.RATE_LIMITER_ACQUIRE_TIMEOUT, Constants.RATE_LIMITER_RATE_ACQUIRE_TIMEUNIT)) {
            log.error("获取不到访问许可，{}", getKey());
            return null;
        }
        return getValueFilter(param);
    }



    /**
     * @param param 查询的参数
     * @return 返回 值
     */
    public V getValueFilter(T param) {
        Pair<RReadWriteLock,RBucket<V>> pair=getValueMutex(param);
        RReadWriteLock readWriteLock=pair.getLeft();
        RBucket<V> rBucket=pair.getRight();
        if (rBucket == null) {
            // 获取数据异常
            return null;
        }
        if(!getBloomFilter().contains(param)){
            return null;
        }
        if (ObjectUtil.isEmpty(rBucket.get())) {
            //未命中
            return getAndCacheData(readWriteLock,rBucket,param);
        }else {
            //命中
            if(isHotData){
                return getValueLogicExpire(readWriteLock,rBucket,param);
            }else {
                return rBucket.get();
            }
        }
    }

    /**
     * 布隆过滤器 解决缓存穿透
     * @return 是否包含
     */
    public  RBloomFilter<T> getBloomFilter(){
        String bloomFilterKey = String.format(Constants.CACHE_PREFIX,String.format(Constants.CACHE_BLOOM_FILTER, getKey()));
        // 布隆过滤器，存在则可能存在，不存在，则一定不存在
        //原理：多个哈希函数映射到多个数组（独热编码采集特征）只记录数组位数表，比哈希表占内存低
        // 存在可能存在：特征全部符合，可能特征采集不够多有误差，特征越多误差越小。不存在一定不存在：特征都不符合
        RBloomFilter<T> bloomFilter = redissonClient.getBloomFilter(bloomFilterKey, StringCodec.INSTANCE);
        // expectedInsertions - - 每个元素的预期插入量 falseProbability - - 预期的错误概率
        bloomFilter.tryInit(Constants.BLOOM_FILTER_EXPECTED_INSERTIONS, Constants.BLOOM_FILTER_FALSE_PROBABILITY);
        return bloomFilter;
    }


    /**
     *
     * 互斥锁  解决缓存击穿
     * @return 读写锁和redis操作对象
     */
    protected Pair<RReadWriteLock,RBucket<V>> getValueMutex(T param) {
        String readWriteKey = String.format(Constants.CACHE_PREFIX,String.format(Constants.CACHE_LOCK, getKey()+param));
        // 读写锁
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(readWriteKey);
        RLock readLock = readWriteLock.readLock();
        readLock.lock();
        RBucket<V> rBucket = null;
        try {
            String cacheKey = String.format(Constants.CACHE_PREFIX, String.format(Constants.CACHE_KEY,getKey()+param));
            // 获取缓存
            rBucket = redissonClient.getBucket(cacheKey, StringCodec.INSTANCE);
        } catch (Exception e) {
            log.error(getKey()+param+"获取缓存失败", e);
        } finally {
            readLock.unlock();
        }
        return Pair.of(readWriteLock,rBucket);
    }

    /**
     * 热点数据逻辑过期，不删数，解决缓存击穿问题
     * @param readWriteLock 读写锁
     * @param rBucket 数据
     * @param param 请求参数
     * @return 返回
     */
    protected V getValueLogicExpire(RReadWriteLock readWriteLock, RBucket<V> rBucket, T param){
        V value=rBucket.get();
        LocalDateTime expireTime;
        try {
            expireTime = (LocalDateTime) Objects.requireNonNull(BeanUtils.findDeclaredMethodWithMinimalParameters(vClass, "getLogicExpireTime")).invoke(value);
        } catch (Exception e) {
            log.error("未设置逻辑过期！");
            return value;
        }
        //5.判断是否过期
        if (expireTime.isAfter(LocalDateTime.now())) {
            return value;
        }
        //已经过期，需要缓存重建
        newSingleThreadExecutor().submit(() -> {
            getAndCacheData(readWriteLock,rBucket,param);
        });
        return value;
    }
    /**
     * 获取 数据库的数据，并且缓存数据
     * @param readWriteLock 锁
     * @param rBucket       redis
     * @param param         查询的参数
     * @return 返回 数据
     */
    protected V getAndCacheData(RReadWriteLock readWriteLock, RBucket<V> rBucket, T param) {
        RLock writeLock = readWriteLock.writeLock();
        // 一直等待 writeLock tryLock(1, 1, TimeUnit.SECONDS);
        writeLock.lock();
        try {
            // 再次验证是否已经缓存，用于并发验证
            V value = rBucket.get();
            if (!ObjectUtil.isEmpty(value)&&!isHotData) {
                return value;
            }
            // 待缓存的数据
            value = willCacheData(param);
            // 缓存数据
            cacheData(rBucket, value);
            // 返回 数据
            return value;
        } catch (Exception e) {
            log.error("获取数据库数据并缓存失败", e);
        } finally {
            writeLock.unlock();
        }
        return null;
    }

    /**
     * 缓存数据
     * 失效时间加随机处理，解决同时失效导致的缓存雪崩
     * null 结果缓存 解决缓存穿透问题
     * @param rBucket bucket
     * @param value   缓存数据
     */
    protected void cacheData(RBucket<V> rBucket, V value) {
        if (ObjectUtil.isEmpty(value)) {
            long timeout = RandomUtils.nextLong(Constants.NULL_VALUE_MIN_EXPIRE_TIME, Constants.NULL_VALUE_MAX_EXPIRE_TIME);
            rBucket.set(null, timeout, Constants.NULL_VALUE_EXPIRE_TIMEUNIT);
        } else {
            if(isHotData){
                rBucket.set(value);
            }else {
                // 缓存
                long timeout = RandomUtils.nextLong(Constants.NONNULL_VALUE_MIN_EXPIRE_TIME, Constants.NONNULL_VALUE_MAX_EXPIRE_TIME);
                rBucket.set(value, timeout, Constants.NONNULL_VALUE_EXPIRE_TIMEUNIT);
            }
        }
    }
}

