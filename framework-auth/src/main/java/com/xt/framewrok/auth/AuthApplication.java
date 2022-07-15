package com.xt.framewrok.auth;

import com.xt.framework.consul.config.MysqlConfig;
import com.xt.framework.interceptor.log.EnableAutoLog;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administrator
 */
@EnableDiscoveryClient
@RestController
@SpringBootApplication
@EnableConfigurationProperties({MysqlConfig.class})
@EnableAutoLog
public class AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}
