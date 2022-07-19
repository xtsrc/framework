package com.xt.framwork.consumer.service;

import com.xt.framwork.common.core.bean.ResultResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author tao.xiong
 * @Description feign 调用
 * @Date 2022/7/8 9:53
 */
@FeignClient(name = "producer")
@Component
public interface DemoService {
    /**
     * feign 调用服务：name+url
     *
     * @return 结果
     */
    @GetMapping("/rpc/uuid")
    ResultResponse<String> getUuid();
}
