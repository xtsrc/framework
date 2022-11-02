package com.xt.framework.interceptor.global.interceptor;

import com.xt.framwork.common.core.constant.Constants;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;

/**
 * @author tao.xiong
 * @Description feign 拦截 自动装配
 * @Date 2022/11/2 17:34
 */
@Configuration
public class FeignInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(Constants.TRACE_ID, MDC.get(Constants.TRACE_ID));

    }
}
