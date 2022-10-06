package com.xt.framework.db;

import com.xt.framework.interceptor.global.annotation.EnableGlobalConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author tao.xiong
 */
@MapperScan("com.xt.**.mapper")
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.xt.framework.**.api"})
@EnableGlobalConfig
public class FrameworkDbApplication {
    public static void main(String[] args) {
        SpringApplication.run(FrameworkDbApplication.class, args);
    }
}
