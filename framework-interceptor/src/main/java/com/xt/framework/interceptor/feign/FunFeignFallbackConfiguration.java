package com.xt.framework.interceptor.feign;

import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import feign.Feign;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.alibaba.sentinel.feign.FunSentinelFeign;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

/**
 * @author tao.xiong
 * @Description 配置
 * @Date 2022/7/9 15:16
 */
@Configuration
public class FunFeignFallbackConfiguration {

    @Bean
    @Scope("prototype")
    @ConditionalOnClass({SphU.class, Feign.class})
    @ConditionalOnProperty(name = "feign.sentinel.enabled")
    @Primary
    public Feign.Builder feignSentinelBuilder() {
        return FunSentinelFeign.builder();
    }

    @Bean
    @Primary
    public BlockExceptionHandler blockExceptionHandler() {
        return new FunUrlBlockHandler();
    }
}
