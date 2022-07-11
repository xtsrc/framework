package com.xt.framework.db;

import com.xt.framework.interceptor.log.EnableAutoLog;
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
@EnableAutoLog
public class FrameworkDbApplication {
    public static void main(String[] args) {
        SpringApplication.run(FrameworkDbApplication.class, args);
    }
}
