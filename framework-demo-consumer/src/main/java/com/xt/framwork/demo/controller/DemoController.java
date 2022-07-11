package com.xt.framwork.demo.controller;

import com.xt.framwork.common.core.bean.ResultResponse;
import com.xt.framwork.demo.service.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @author tao.xiong
 * @Description 服务测试
 * @Date 2022/7/7 16:16
 */
@Slf4j
@RestController
public class DemoController {
    @Resource
    private RestTemplate restTemplate;

    @Resource
    private DemoService demoService;

    /**
     * Eureka 注册服务发现
     * Ribbon 负载均衡
     *
     * @return 结果
     */
    @GetMapping("/v1/id")
    public ResponseEntity<String> getId() {
        //根据注册名称获取
        ResponseEntity<String> result = restTemplate.getForEntity("http://demo-producer/v1/uuid", String.class);
        String uuid = result.getBody();
        log.info("request id: {}", uuid);
        assert uuid != null;
        return ResponseEntity.ok(uuid);
    }

    @GetMapping("/feign/id")
    public ResultResponse<String> getIdByFeign() {
        ResultResponse<String> result = demoService.getUuid();
        String uuid = result.getResult();
        log.info("request id: {}", uuid);
        assert uuid != null;
        return ResultResponse.success(uuid);
    }
}
