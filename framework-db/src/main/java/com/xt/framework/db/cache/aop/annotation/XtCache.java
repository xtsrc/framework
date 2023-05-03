package com.xt.framework.db.cache.aop.annotation;

import org.springframework.boot.autoconfigure.cache.CacheType;

import java.lang.annotation.*;

/**
 * @author tao.xiong
 * @date 2023/2/17 16:20
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface XtCache {
    String cacheName();
    String key();
}
