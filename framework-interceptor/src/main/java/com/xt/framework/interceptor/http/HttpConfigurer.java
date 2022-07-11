package com.xt.framework.interceptor.http;

import com.xt.framwork.common.core.constant.Constants;
import okhttp3.Interceptor;
import okhttp3.Request;
import org.apache.http.HttpRequestInterceptor;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.ClientHttpRequestInterceptor;

/**
 * @author tao.xiong
 * @Description http拦截器
 * @Date 2022/2/25 14:03
 */
@Configuration
public class HttpConfigurer {

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
