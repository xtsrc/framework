package com.xt.framewrok.auth.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tao.xiong
 * @Description 登录请求
 * @Date 2022/8/22 9:58
 */
@RestController
@Slf4j
public class LoginController {
    @GetMapping("/login")
    public String login() {
        log.info("登录");
        return "login";
    }

    @GetMapping("/getValue")
    public String getValue() {
        return "value";
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }
}
