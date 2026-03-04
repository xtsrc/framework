package com.xt.framework.db.controller;

import com.xt.framework.db.redis.core.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tao.xiong
 * @Description
 * @Date 2026/3/3 10:00
 */
@RestController
@Slf4j
@RequestMapping("/redis")
public class RedisTestController {
    @GetMapping("/delay")
    public void  delay(){
        RedisUtil.offerAsync();
    }
}
