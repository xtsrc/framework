package com.xt.framework.oss;

import com.xt.framework.interceptor.global.annotation.EnableGlobalConfig;
import com.xt.framework.oss.util.OssUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author tao.xiong
 */
@SpringBootApplication
@EnableDiscoveryClient
@RestController
@EnableGlobalConfig
public class FrameworkOssApplication {
    public static void main(String[] args) {
        SpringApplication.run(FrameworkOssApplication.class, args);
    }

    @Resource
    private OssUtil ossUtil;

    @GetMapping("/oss/qrc")
    public String getByKey(@RequestParam("text") String text) throws Exception {
        return ossUtil.generateQrCodeAndUpload(text);
    }
}
