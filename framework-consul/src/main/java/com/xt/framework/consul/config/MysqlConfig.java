package com.xt.framework.consul.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author tao.xiong
 */
@Component
@ConfigurationProperties(prefix = "spring.datasource")
@Data
public class MysqlConfig {
    private String url;
    private String username;
    private String password;
}
