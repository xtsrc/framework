package com.xt.framework.job;

import com.xt.framework.interceptor.global.annotation.EnableGlobalConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@EnableDiscoveryClient
@RestController
@SpringBootApplication()
@EnableGlobalConfig
public class JobApplication {
    @Resource
    private Environment environment;

    @GetMapping("/consul/getValue")
    public String getByKey(@RequestParam("key") String key) {
        return environment.getProperty(key);
    }

    public static void main(String[] args) {
        SpringApplication.run(JobApplication.class, args);
    }
}
