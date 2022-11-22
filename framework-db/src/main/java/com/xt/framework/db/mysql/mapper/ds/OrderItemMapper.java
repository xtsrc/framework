package com.xt.framework.db.mysql.mapper.ds;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xt.framework.db.mysql.mapper.ds.model.OrderItem;

/**
 * @author tao.xiong
 * @Description 订单明细
 * @Date 2022/11/17 13:57
 */
@DS("ds-0")
public interface OrderItemMapper extends BaseMapper<OrderItem> {
}
