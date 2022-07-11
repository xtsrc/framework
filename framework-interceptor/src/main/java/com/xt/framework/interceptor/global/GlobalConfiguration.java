package com.xt.framework.interceptor.global;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author tao.xiong
 * @Description
 * @Date 2022/7/11 16:30
 */
@Configuration
public class GlobalConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public GlobalHandler<Object> globalExceptionHandler() {
        return new GlobalHandler<>();
    }
}
