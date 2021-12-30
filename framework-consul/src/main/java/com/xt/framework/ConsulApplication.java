package com.xt.framework;

import com.xt.framework.config.UserConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableDiscoveryClient
@RestController
@SpringBootApplication
@EnableConfigurationProperties({UserConfig.class })
public class ConsulApplication {
    @Value("${description}")
    private String description;

    @Autowired
    private UserConfig userConfig;


    @GetMapping("/health")
    public String Health() {
        System.out.println("health");
        return "OK";
    }

    @GetMapping("/user/description")
    public String Description() {
        return description;
    }

    @GetMapping("/user/intro")
    public String StudentIntro() {
        return userConfig.toString();
    }

    public static void main(String[] args) {
        SpringApplication.run(ConsulApplication.class, args);
    }
}
