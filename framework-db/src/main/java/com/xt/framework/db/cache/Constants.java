package com.xt.framework.db.cache;

import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;

import java.util.concurrent.TimeUnit;

/**
 * @author tao.xiong
 * @Description 常量
 * @Date 2022/12/6 14:34
 */
public class Constants {
    private Constants() {
        throw new IllegalStateException("Utility class");
    }
    public static final String CACHE_KEY = "key:%s";

    public static final String CACHE_LOCK = "lock:%s";

    public static final String CACHE_BLOOM_FILTER = "bloomFilter:%s";

    public static final String CACHE_RATE_LIMITER = "rateLimiter:%s";


    /**
     * 缓存前缀，必须包含占位符 %s
     */
    public static final String CACHE_PREFIX = "xt:cache:%s";

    /**
     * 限流的速率类型
     */
    protected static RateType RATE_LIMITER_RATE_TYPE = RateType.OVERALL;

    /**
     * 限流的速率
     */
    protected static long RATE_LIMITER_RATE = 2;

    /**
     * 限流速率间隔
     */
    protected static long RATE_LIMITER_RATE_INTERVAL = 1;

    /**
     * 限流速率间隔单位
     */
    protected static RateIntervalUnit RATE_LIMITER_RATE_INTERVAL_UNIT = RateIntervalUnit.SECONDS;

    /**
     * 限流尝试获取超时时间
     */
    protected static long RATE_LIMITER_ACQUIRE_TIMEOUT = 1;

    /**
     * 限流尝试获取超时时间单位
     */
    protected static TimeUnit RATE_LIMITER_RATE_ACQUIRE_TIMEUNIT = TimeUnit.SECONDS;

    /**
     * 布隆过滤器预期元素插入量
     */
    protected static long BLOOM_FILTER_EXPECTED_INSERTIONS = 2 ^ 32;

    /**
     * 布隆过滤器预期的错误概率
     */
    protected static double BLOOM_FILTER_FALSE_PROBABILITY = 0.01;

    /**
     * 空值是最小的过期时间
     */
    protected static long NULL_VALUE_MIN_EXPIRE_TIME = 1;

    /**
     * 空值是最大的过期时间
     */
    protected static long NULL_VALUE_MAX_EXPIRE_TIME = 2;

    /**
     * 空值是过期时间的单位
     */
    protected static TimeUnit NULL_VALUE_EXPIRE_TIMEUNIT = TimeUnit.SECONDS;

    /**
     * 非空值是最小的过期时间
     */
    protected static long NONNULL_VALUE_MIN_EXPIRE_TIME = 3;

    /**
     * 非空值是最大的过期时间
     */
    protected static long NONNULL_VALUE_MAX_EXPIRE_TIME = 10;

    /**
     * 非空值是过期时间的单位
     */
    protected static TimeUnit NONNULL_VALUE_EXPIRE_TIMEUNIT = TimeUnit.MINUTES;
}
