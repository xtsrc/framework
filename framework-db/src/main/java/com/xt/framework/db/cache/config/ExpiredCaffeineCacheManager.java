package com.xt.framework.db.cache.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.xt.framwork.common.core.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.cache.Cache;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;

import java.util.concurrent.TimeUnit;

/**
 * @author luojiaheng
 * @date 2019/11/22 14:31
 */
@Slf4j
public class ExpiredCaffeineCacheManager extends CaffeineCacheManager {

    public ExpiredCaffeineCacheManager() {
        super();
    }

    @Override
    protected @NotNull Cache createCaffeineCache(@NotNull String name) {
        String[] strArr = parseCacheName(name);
        if (strArr.length == 1) {
            return super.createCaffeineCache(name);
        } else if (strArr.length == 2) {
            return new CaffeineCache(name, createNativeCaffeineCache(name, strArr[1], null), isAllowNullValues());
        } else if (strArr.length == 3) {
            return new CaffeineCache(name, createNativeCaffeineCache(name, strArr[1], Long.valueOf(strArr[2])), isAllowNullValues());
        } else {
            return new CaffeineCache(name, createNativeCaffeineCache(name), isAllowNullValues());
        }
    }

    private String[] parseCacheName(String name) {
        if (StringUtils.isBlank(name)) {
            throw new BizException("缓存必需指定名称");
        }
        return StringUtils.split(name, "#");
    }

    private com.github.benmanes.caffeine.cache.Cache<Object, Object> createNativeCaffeineCache(String name, String cacheExpired, Long cacheMaxsize) {
        Caffeine<Object, Object> caffeine = Caffeine.newBuilder();
        if (cacheExpired != null) {
            if (StringUtils.startsWith(cacheExpired, "w")) {
                caffeine.expireAfterWrite(Long.parseLong(cacheExpired.substring(1)), TimeUnit.MINUTES);
            } else if (StringUtils.startsWith(cacheExpired, "r")) {
                caffeine.expireAfterAccess(Long.parseLong(cacheExpired.substring(1)), TimeUnit.MINUTES);
            } else {
                caffeine.expireAfterWrite(Long.parseLong(cacheExpired), TimeUnit.MINUTES);
            }
        }
        if (cacheMaxsize != null) {
            caffeine.maximumSize(cacheMaxsize);
        }
        log.info("创建缓存, name: {}, expire: {}, size: {}", name, cacheExpired, cacheMaxsize);
        return caffeine.build();
    }

}
