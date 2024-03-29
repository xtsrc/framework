package com.xt.framework.consul;

import com.xt.framework.consul.config.MysqlConfig;
import com.xt.framework.interceptor.global.annotation.EnableGlobalConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author tao.xiong
 */
@EnableDiscoveryClient
@RestController
@SpringBootApplication
@EnableConfigurationProperties({MysqlConfig.class})
@EnableFeignClients(basePackages = {"com.xt.framework.**.api"})
@EnableGlobalConfig
public class ConsulApplication {
    @Value("${description}")
    private String description;
    @Resource
    private Environment environment;

    @Resource
    private MysqlConfig mysqlConfig;


    @GetMapping("/health")
    public String health() {
        return "OK";
    }

    @GetMapping("/user/description")
    public String description() {
        return description;
    }

    @GetMapping("/mysql/intro")
    public String mysqlIntro() {
        return mysqlConfig.toString();
    }
    @GetMapping("/consul/getValue")
    public String getByKey(@RequestParam("key") String key) {
        return environment.getProperty(key);
    }

    public static void main(String[] args) {
        SpringApplication.run(ConsulApplication.class, args);
    }
}
