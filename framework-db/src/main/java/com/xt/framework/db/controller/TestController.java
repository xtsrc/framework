package com.xt.framework.db.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xt.framework.db.mysql.mapper.model.Order;
import com.xt.framework.db.mysql.mapper.model.OrderItem;
import com.xt.framework.db.mysql.mapper.model.Log;
import com.xt.framework.db.mysql.service.IOrderItemService;
import com.xt.framework.db.mysql.service.IOrderService;
import com.xt.framework.db.mysql.service.dto.OrderCreateReq;
import com.xt.framework.db.mysql.service.ILogService;
import com.xt.framwork.common.core.bean.ResultResponse;
import com.xt.framwork.common.core.util.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
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
    @Cacheable("xt:framework:test:last_log#5")
    public ResultResponse<Log> latestLog() {
        log.info("查询最近一条日志");
        Log log = logService.getOne(Wrappers.<Log>lambdaQuery().orderByDesc(Log::getCreateTime).last("limit 1"));
        return ResultResponse.success(log);
    }

    @RequestMapping("/sharding/insertOrder")
    public ResultResponse<Boolean> insertOrder(@RequestBody OrderCreateReq orderCreateReq) {
        Order order = new Order();
        BeanUtil.copyProperties(orderCreateReq, order);
        orderService.save(order);
        orderCreateReq.getItems().forEach(item -> {
            OrderItem orderItem = new OrderItem();
            BeanUtil.copyProperties(item, orderItem);
            orderItem.setOrderNo(order.getOrderNo());
            orderItemService.save(orderItem);
        });
        return ResultResponse.success(Boolean.TRUE);
    }
}
