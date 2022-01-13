package com.xt.framework;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author tao.xiong
 */
@MapperScan("com.xt.**.mapper")
@SpringBootApplication
@EnableDiscoveryClient
public class FrameworkDbApplication {
    public static void main(String[] args) {
        SpringApplication.run(FrameworkDbApplication.class, args);
    }
}
