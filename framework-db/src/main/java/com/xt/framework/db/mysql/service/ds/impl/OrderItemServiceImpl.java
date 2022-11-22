package com.xt.framework.db.mysql.service.ds.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xt.framework.db.mysql.mapper.ds.OrderItemMapper;
import com.xt.framework.db.mysql.mapper.ds.model.OrderItem;
import com.xt.framework.db.mysql.service.ds.IOrderItemService;
import org.springframework.stereotype.Service;

/**
 * @author tao.xiong
 * @Description 订单服务实现
 * @Date 2022/11/17 14:00
 */
@Service
@DS("ds-0")
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements IOrderItemService {
}
