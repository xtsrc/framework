package com.xt.framework.producer;

import com.xt.framework.interceptor.log.EnableAutoLog;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author tao.xiong
 * @Description
 * @Date 2022/7/7 16:11
 */
@EnableAutoLog
@EnableDiscoveryClient
@SpringBootApplication
public class ProducerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProducerApplication.class, args);
    }
}