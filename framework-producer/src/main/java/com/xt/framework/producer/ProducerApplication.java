package com.xt.framework.producer;

import com.xt.framework.interceptor.global.annotation.EnableGlobalConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author tao.xiong
 * @Description
 * @Date 2022/7/7 16:11
 */
@EnableGlobalConfig
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.xt.framework.**.api"})
public class ProducerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProducerApplication.class, args);
    }
}
