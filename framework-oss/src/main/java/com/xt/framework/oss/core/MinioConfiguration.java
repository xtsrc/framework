package com.xt.framework.oss.core;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author tao.xiong
 * @Description 配置注入
 * @Date 2022/6/30 10:17
 */
@Slf4j
@Configuration
public class MinioConfiguration {
    @Resource
    private MinioProperties minioProperties;

    @Bean
    public MinioClient minioClient() throws Exception {
        MinioClient client = MinioClient.builder()
                .endpoint(minioProperties.getUrl())
                .credentials(minioProperties.getAccess(), minioProperties.getSecret())
                .build();
        initBucket(client, minioProperties.getBucket());
        return client;
    }

    private void initBucket(MinioClient minioClient, String bucket) throws Exception {
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
        if (!found) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            log.info("成功创建 Bucket [{}]", bucket);
        }
    }
}
