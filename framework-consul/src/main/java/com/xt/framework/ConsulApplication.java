package com.xt.framework;

import com.xt.framework.config.MysqlConfig;
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
@EnableConfigurationProperties({MysqlConfig.class})
public class ConsulApplication {
    @Value("${description}")
    private String description;

    @Autowired
    private MysqlConfig mysqlConfig;


    @GetMapping("/health")
    public String Health() {
        System.out.println("health");
        return "OK";
    }

    @GetMapping("/user/description")
    public String Description() {
        return description;
    }

    @GetMapping("/mysql/intro")
    public String MysqlIntro() {
        return mysqlConfig.toString();
    }

    public static void main(String[] args) {
        SpringApplication.run(ConsulApplication.class, args);
    }
}
