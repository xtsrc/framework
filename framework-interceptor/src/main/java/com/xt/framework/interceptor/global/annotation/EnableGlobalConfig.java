package com.xt.framework.interceptor.global.annotation;

import com.xt.framework.interceptor.global.configurer.GlobalConfigurer;
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
@Import({GlobalConfigurer.class})
public @interface EnableGlobalConfig {
    /**
     * Filter/OncePerRequestFilter： 可以拿到原始的HTTP请求和响应信息，拿不到处理请求的方法值信息
     * interceptor：既可以拿到HTTP请求和响应信息，也可以拿到请求的方法信息，拿不到方法调用的参数值信息
     * RequestBodyAdvice和ResponseBodyAdvice：前者拦截不到无@RequestBody的方法，后者拦截不到无@ResponseBody的方法
     * aspect：可以拿到请求方法的传入参数值，拿不到原始的HTTP请求和响应的对象
     */
   /* Filter-start
        Interceptor-start
          ControllerAdvice-start
            Aspect-start
              Controller-start
              Controller-end
            Aspect-end
          ControllerAdvice-end
        Interceptor-end
      Filter-end*/
}
