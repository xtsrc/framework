package com.xt.framework.producer.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author tao.xiong
 * @Description 服务测试
 * @Date 2022/7/7 16:16
 */
@Slf4j
@RestController
public class DemoController {
    @GetMapping("/v1/uuid")
    public ResponseEntity<String> getUuid() {
        String uuid = UUID.randomUUID().toString();
        log.info("generate uuid: {}", uuid);
        return ResponseEntity.ok(uuid);
    }
    @GetMapping("/high")
    @PreAuthorize("hasAuthority('ROLE_HIGH')")
    public String normal( ) {
        return "high permission";
    }

    @GetMapping("/mid")
    @PreAuthorize("hasAuthority('ROLE_MID')")
    public String medium() {
        return "mid permission";
    }

    @GetMapping("/low")
    @PreAuthorize("hasAuthority('ROLE_LOW')")
    public String admin() {
        return "low permission";
    }

    @GetMapping("/callback")
    public String login() {
        return "success";
    }

}
