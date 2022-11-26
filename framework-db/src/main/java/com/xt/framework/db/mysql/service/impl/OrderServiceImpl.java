package com.xt.framework.db.mysql.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xt.framework.db.mysql.mapper.OrderMapper;
import com.xt.framework.db.mysql.mapper.model.Order;
import com.xt.framework.db.mysql.service.IOrderService;
import org.springframework.stereotype.Service;

/**
 * @author tao.xiong
 * @Description 订单服务实现
 * @Date 2022/11/17 14:00
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {
}
