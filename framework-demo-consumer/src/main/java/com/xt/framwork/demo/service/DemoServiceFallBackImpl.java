package com.xt.framwork.demo.service;

import com.xt.framwork.common.core.bean.ResultResponse;
import org.springframework.stereotype.Component;

/**
 * @author tao.xiong
 * @Description 服务降级
 * @Date 2022/7/8 9:59
 */
@Component
public class DemoServiceFallBackImpl implements DemoService{
    @Override
    public ResultResponse<String> getUuid() {
        return ResultResponse.fail("远程服务不可用请稍后重试！");
    }
}
