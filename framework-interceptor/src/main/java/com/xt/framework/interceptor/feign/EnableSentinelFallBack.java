package com.xt.framework.interceptor.feign;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author tao.xiong
 * @Description 注解
 * @Date 2022/7/9 15:18
 */
@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = {java.lang.annotation.ElementType.TYPE})
@Documented
@Import({FunFeignFallbackConfiguration.class})
public @interface EnableSentinelFallBack {
    //nothing
}
