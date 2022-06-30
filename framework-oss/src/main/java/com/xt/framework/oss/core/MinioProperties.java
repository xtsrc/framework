package com.xt.framework.oss.core;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author tao.xiong
 * @Description 配置
 * @Date 2022/6/30 10:18
 */
@Data
@ConfigurationProperties(prefix = "minio")
@Component
public class MinioProperties {
    private String url;
    private String bucket;
    private String access;
    private String secret;
}
