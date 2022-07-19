package com.xt.framework.producer.api.service;

import com.xt.framwork.common.core.bean.ResultResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author tao.xiong
 * @Description feign 测试
 * @Date 2022/7/12 16:36
 */
@Component
@FeignClient(value = "producer")
public interface IDemoService {
    @GetMapping("/rpc/uuid")
    ResultResponse<String> getUuid();
}
