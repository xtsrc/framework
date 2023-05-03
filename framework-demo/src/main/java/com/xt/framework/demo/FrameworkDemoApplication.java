package com.xt.framework.demo;

import com.xt.framework.interceptor.global.annotation.EnableAsync;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author tao.xiong
 * @date 2023/4/11 15:45
 * @desc
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.xt.framework.**.api"})
@EnableAsync
public class FrameworkDemoApplication {
}
