package com.xt.framework.db.redis.core;


import com.xt.framework.db.redis.util.SpringBeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * @author tao.xiong
 * @Description redis 工具
 * @Date 2022/5/9 11:12
 */
@Slf4j
public class RedisUtil {
    @Resource
    protected static final RedisTemplate<String, Object> JEDIS_TEMPLATE = SpringBeanUtil.getBean("jedisTemplate");
    @Resource
    protected RedissonClient redissonClient;
    private static final String LOCK_KEY = "redis_lock";
    private static final long EXPIRE_TIME = 30000;

    public static RedisTemplate<String, Object> inst() {
        return JEDIS_TEMPLATE;
    }

    private RedisUtil() {
        throw new IllegalStateException("utility class");
    }

    /**
     * 加锁，无阻塞
     *
     * @param key   加锁id 同一个key表示同一个临界资源
     * @param value 锁占用唯一标识
     * @return 加锁结果
     */
    public static Boolean tryLock(String key, String value) {
        try {
            //SET命令返回OK ，则证明获取锁成功
            assert JEDIS_TEMPLATE != null;
            return JEDIS_TEMPLATE.opsForValue().setIfAbsent(LOCK_KEY + key, value, EXPIRE_TIME, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 解锁
     *
     * @param key   解锁id
     * @param value 锁占用唯一标识
     * @return 解锁结果
     */
    public static Boolean unlock(String key, String value) {
        String success = "1";
        try {
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            RedisScript<String> redisScript = new DefaultRedisScript<>(script, String.class);
            //redis脚本执行
            assert JEDIS_TEMPLATE != null;
            Object result = JEDIS_TEMPLATE.execute(redisScript, Collections.singletonList(LOCK_KEY + key), value);
            if (success.equals(result)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * 加锁
     */
    public void lock(String lockKey) {
        redissonClient.getLock(lockKey).lock();
    }

    /**
     * 释放锁
     */
    public void unLock(String lockKey) {
        redissonClient.getLock(lockKey).unlock();
    }
}
