package com.xt.framework.db.mysql.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xt.framework.db.mysql.mapper.OrderItemMapper;
import com.xt.framework.db.mysql.mapper.model.OrderItem;
import com.xt.framework.db.mysql.service.IOrderItemService;
import org.springframework.stereotype.Service;

/**
 * @author tao.xiong
 * @Description 订单服务实现
 * @Date 2022/11/17 14:00
 */
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements IOrderItemService {
}
