package com.xt.framework.interceptor.global.configurer;

import com.xt.framework.interceptor.global.advice.GlobalRequestAdvice;
import com.xt.framework.interceptor.global.advice.GlobalResponseAdvice;
import com.xt.framework.interceptor.global.aspect.AopWebLogAspect;
import com.xt.framework.interceptor.global.filter.MyOnceFilter;
import com.xt.framework.interceptor.global.filter.TraceFilter;
import com.xt.framework.interceptor.global.interceptor.LogInterceptor;
import com.xt.framwork.common.core.constant.Constants;
import okhttp3.Interceptor;
import okhttp3.Request;
import org.apache.http.HttpRequestInterceptor;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author tao.xiong
 * @Description web 组件配置
 * @Date 2022/7/11 16:30
 */
@Configuration
public class GlobalConfigurer implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getLogInterceptor())
                .addPathPatterns("/**")
                //不排除会循环调用
                .excludePathPatterns("/**/log/**", "/error");
    }

    /**
     * interceptor 既可以拿到HTTP请求和响应信息，也可以拿到请求的方法信息，拿不到方法调用的参数值信息
     */
    @Bean
    @ConditionalOnMissingBean
    public LogInterceptor getLogInterceptor() {
        return new LogInterceptor();
    }


    @Bean
    @Primary
    public Interceptor okHttpInterceptor() {
        return chain -> {
            String traceId = MDC.get(Constants.TRACE_ID);
            Request request = chain.request();
            if (traceId != null) {
                request = request.newBuilder().addHeader(Constants.TRACE_ID, traceId).build();
            }
            return chain.proceed(request);
        };
    }

    @Bean
    @Primary
    public ClientHttpRequestInterceptor restTemplateInterceptor() {
        return (httpRequest, bytes, clientHttpRequestExecution) -> {
            String traceId = MDC.get(Constants.TRACE_ID);
            if (traceId != null) {
                httpRequest.getHeaders().add(Constants.TRACE_ID, traceId);
            }

            return clientHttpRequestExecution.execute(httpRequest, bytes);
        };
    }

    @Bean
    @Primary
    public HttpRequestInterceptor httpClientInterceptor() {
        return (httpRequest, httpContext) -> {
            String traceId = MDC.get(Constants.TRACE_ID);
            //当前线程调用中有traceId，则将该traceId进行透传
            if (traceId != null) {
                //添加请求体
                httpRequest.addHeader(Constants.TRACE_ID, traceId);
            }
        };
    }

    /**
     * advice 主要是用于全局的异常拦截和处理
     */
    @Bean
    @ConditionalOnMissingBean
    public GlobalResponseAdvice<Object> globalResponseAdvice() {
        return new GlobalResponseAdvice<>();
    }

    @Bean
    @ConditionalOnMissingBean
    public GlobalRequestAdvice globalRequestAdvice() {
        return new GlobalRequestAdvice();
    }

    /**
     * aspect 可以拿到请求方法的传入参数值，拿不到原始的HTTP请求和响应的对象
     */
    @Bean
    @ConditionalOnMissingBean
    public AopWebLogAspect aopWebLogAspect() {
        return new AopWebLogAspect();
    }

    /**
     * springboot当中提供了FilterRegistrationBean类来注册Filter
     * 可以拿到原始的HTTP请求和响应信息，拿不到处理请求的方法值信息
     */
    @Bean
    public FilterRegistrationBean<TraceFilter> filterTraceRegistration() {
        FilterRegistrationBean<TraceFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(traceFilter());
        registration.addUrlPatterns("/*");
        registration.addInitParameter("paramName", "paramValue");
        registration.setName("traceFilter");
        registration.setOrder(1);
        return registration;
    }

    @Bean
    public FilterRegistrationBean<MyOnceFilter> filterOnceRegistration() {
        FilterRegistrationBean<MyOnceFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(myOnceFilter());
        registration.addUrlPatterns("/*");
        registration.addInitParameter("paramName", "paramValue");
        registration.setName("myOnceFilter");
        registration.setOrder(0);
        return registration;
    }

    @Bean
    public TraceFilter traceFilter() {
        return new TraceFilter();
    }

    @Bean
    @ConditionalOnMissingBean
    public MyOnceFilter myOnceFilter() {
        return new MyOnceFilter();
    }
}
