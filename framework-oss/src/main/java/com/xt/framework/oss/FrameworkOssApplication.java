package com.xt.framework.oss;
import log.EnableAutoLog;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author tao.xiong
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableAutoLog
public class FrameworkOssApplication {
    public static void main(String[] args) {
        SpringApplication.run(FrameworkOssApplication.class, args);
    }
}
