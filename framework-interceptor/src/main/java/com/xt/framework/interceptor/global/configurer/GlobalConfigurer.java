package com.xt.framework.interceptor.global.configurer;

import com.xt.framework.interceptor.global.advice.GlobalRequestAdvice;
import com.xt.framework.interceptor.global.advice.GlobalResponseAdvice;
import com.xt.framework.interceptor.global.interceptor.LogInterceptor;
import com.xt.framwork.common.core.constant.Constants;
import okhttp3.Interceptor;
import okhttp3.Request;
import org.apache.http.HttpRequestInterceptor;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author tao.xiong
 * @Description
 * @Date 2022/7/11 16:30
 */
@Configuration
public class GlobalConfigurer implements WebMvcConfigurer {
    @Bean
    public LogInterceptor getLogInterceptor(){
        return new LogInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getLogInterceptor())
                .addPathPatterns("/**")
                //不排除会循环调用
                .excludePathPatterns("/db/saveLogInfo");
    }

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
}
