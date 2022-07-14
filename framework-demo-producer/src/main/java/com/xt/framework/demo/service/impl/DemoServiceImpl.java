package com.xt.framework.demo.service.impl;

import com.xt.framework.demo.api.service.IDemoService;
import com.xt.framwork.common.core.bean.ResultResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author tao.xiong
 * @Description 实现类
 * @Date 2022/7/12 16:40
 */
@Slf4j
@RestController
public class DemoServiceImpl implements IDemoService {
    @Override
    public ResultResponse<String> getUuid() {
        String uuid = UUID.randomUUID().toString();
        log.info("generate uuid: {}", uuid);
        return ResultResponse.success(uuid);
    }
}
