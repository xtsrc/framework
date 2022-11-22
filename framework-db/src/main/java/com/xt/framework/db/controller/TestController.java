package com.xt.framework.db.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xt.framework.db.mysql.mapper.ds.model.Order;
import com.xt.framework.db.mysql.mapper.framework.model.Log;
import com.xt.framework.db.mysql.service.ds.IOrderItemService;
import com.xt.framework.db.mysql.service.ds.IOrderService;
import com.xt.framework.db.mysql.service.dto.OrderCreateReq;
import com.xt.framework.db.mysql.service.framework.ILogService;
import com.xt.framwork.common.core.bean.ResultResponse;
import com.xt.framwork.common.core.util.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author tao.xiong
 * @Description 测试调用
 * @Date 2022/5/13 11:04
 */
@RestController
@Slf4j
public class TestController {
    @Resource
    private ILogService logService;
    @Resource
    private IOrderService orderService;
    @Resource
    private IOrderItemService orderItemService;

    @GetMapping("/health")
    public ResultResponse<String> health(@RequestParam("key") String key) {
        log.info("测试请求：{}", key);
        return ResultResponse.success(key);
    }

    @GetMapping("/log/latest")
    public ResultResponse<Log> latestLog() {
        log.info("查询最近一条日志");
        Log log = logService.getOne(Wrappers.<Log>lambdaQuery().orderByDesc(Log::getCreateTime).last("limit 1"));
        return ResultResponse.success(log);
    }

    @RequestMapping("/sharding/insertOrder")
    public ResultResponse<Long> insertOrder(@RequestBody OrderCreateReq orderCreateReq) {
        Order order = new Order();
        BeanUtil.copyProperties(orderCreateReq, order);
        orderService.save(order);
        return ResultResponse.success(1L);
    }
}
