package com.xt.framework.oss.core;

import io.minio.*;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.InputStream;

/**
 * @author tao.xiong
 * @Description oss 服务器
 * @Date 2022/6/29 10:20
 */
@Slf4j
@Service
public class MinioService {
    @Resource
    private MinioClient minioClient;
    @Value("${minio.bucket}")
    private String bucket;

    /**
     * 上传文件
     *
     * @param is          输入流
     * @param object      对象（文件）名
     * @param contentType 文件类型
     */
    public void putObject(InputStream is, String object, String contentType) throws Exception {
        long start = System.currentTimeMillis();
        // 不得小于 5 Mib
        int partSize = 1024 * 1024 * 10;
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucket)
                .object(object)
                .contentType(contentType)
                .stream(is, -1, partSize)
                .build());
        log.info("成功上传文件至云端 [{}]，耗时 [{} ms]", object, System.currentTimeMillis() - start);
    }

    /**
     * 获取文件流
     *
     * @param object 对象（文件）名
     * @return 文件流
     */
    public GetObjectResponse getObject(String object) throws Exception {
        long start = System.currentTimeMillis();
        GetObjectResponse response = minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucket)
                .object(object)
                .build());
        log.info("成功获取 Object [{}]，耗时 [{} ms]", object, System.currentTimeMillis() - start);
        return response;
    }

    /**
     * 删除对象（文件）
     *
     * @param object 对象（文件名）
     */
    public void removeObject(String object) throws Exception {
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(bucket)
                .object(object)
                .build());
        log.info("成功删除 Object [{}]", object);
    }

    /**
     * 获取外链
     * @param objectName 文件名
     * @return url
     */
    public String getFileUrl(String objectName) {
        try {
            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .method(Method.GET)
                    .build());
        } catch (Exception e) {
            log.error("{}文件获取失败", objectName);
            return "";
        }
    }
}

