package com.xt.framework.db.cache.config;

import com.alibaba.fastjson.JSON;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 *
 */
@Configuration
@EnableCaching
public class CaffeineCacheConfig extends CachingConfigurerSupport {

    @Override
    @Bean
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> JSON.toJSONString(params);
    }

    @Override
    @Bean
    public CacheManager cacheManager() {
        ExpiredCaffeineCacheManager cacheManager = new ExpiredCaffeineCacheManager();
        Caffeine<Object, Object> caffeine = Caffeine.newBuilder()
                //cache的初始容量值
                .initialCapacity(100)
                //maximumSize用来控制cache的最大缓存数量，maximumSize和maximumWeight不可以同时使用，
                .maximumSize(1000).expireAfterAccess(30, TimeUnit.MINUTES);
        cacheManager.setCaffeine(caffeine);
        return cacheManager;
    }

}
