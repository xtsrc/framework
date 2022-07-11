package com.xt.framework.interceptor.global;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author tao.xiong
 * @Description 全局异常处理注解
 * @Date 2022/7/11 16:29
 */
@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = {java.lang.annotation.ElementType.TYPE})
@Documented
@Import({GlobalConfiguration.class})
public @interface EnableGlobalConfig {
    //nothing
}
