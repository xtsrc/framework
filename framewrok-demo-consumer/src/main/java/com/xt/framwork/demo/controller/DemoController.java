package com.xt.framwork.demo.controller;

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
    @GetMapping("/v1/id")
    public ResponseEntity<String> getId() {
        ResponseEntity<String> result = restTemplate.getForEntity("http://demo-producer/v1/uuid", String.class);
        String uuid = result.getBody();
        log.info("request id: {}", uuid);
        assert uuid != null;
        return ResponseEntity.ok(uuid);
    }
}
