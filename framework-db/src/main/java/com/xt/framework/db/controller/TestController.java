package com.xt.framework.db.controller;

import com.xt.framwork.common.core.bean.ResultResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tao.xiong
 * @Description 测试调用
 * @Date 2022/5/13 11:04
 */
@RestController
@Slf4j
public class TestController {
    @GetMapping("/health")
    public ResultResponse<String> health(@RequestParam("key") String key) {
        log.info("测试请求：{}",key);
        return ResultResponse.success(key);
    }
}
