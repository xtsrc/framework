package com.xt.framework.interceptor.global.annotation;

import com.xt.framework.interceptor.global.configurer.BaseAsyncConfigurer;
import com.xt.framework.interceptor.global.configurer.GuavaEventBusConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author tao.xiong
 * @date 2023/4/11 15:54
 * @desc 自定义异步
 */
@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = {java.lang.annotation.ElementType.TYPE})
@Documented
@Import({BaseAsyncConfigurer.class, GuavaEventBusConfig.class})
public @interface EnableAsync {
}
