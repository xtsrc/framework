package com.xt.framework.demo;

import log.EnableAutoLog;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author tao.xiong
 * @Description
 * @Date 2022/7/7 16:11
 */
@EnableAutoLog
@EnableEurekaClient
@SpringBootApplication
public class ProducerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProducerApplication.class, args);
    }
}
