package com.xt.framwork.consumer;

import com.xt.framework.interceptor.feign.EnableSentinelFallBack;
import com.xt.framework.interceptor.global.annotation.EnableGlobalConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author tao.xiong
 * @Description
 * @Date 2022/7/7 16:40
 * EnableDiscoveryClient 可以是任一注册中心
 */
@EnableGlobalConfig
@EnableSentinelFallBack
@EnableEurekaClient
@EnableFeignClients/*(basePackages = "com.xt.framework.*")*/
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class ConsumerApplication {

    /**
     * @return Ribbon 负载均衡
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }
}
