package com.xt.framework.db.mysql.mapper.ds;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xt.framework.db.mysql.mapper.ds.model.Order;

/**
 * @author tao.xiong
 * @Description 订单
 * @Date 2022/11/17 13:57
 */
@DS("ds-0")
public interface OrderMapper extends BaseMapper<Order> {
}
