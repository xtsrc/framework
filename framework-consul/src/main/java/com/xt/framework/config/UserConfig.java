package com.xt.framework.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "user")
public class UserConfig {
    private String name;
    private Integer age;
}
