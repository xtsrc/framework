package com.xt.framework.common.core.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * @author tao.xiong
 * @Description 查询回调
 * @Date 2021/7/13 18:14
 */
@FunctionalInterface
public interface QueryCallBack<R, T> {
    /**
     * 构建条件查询
     *
     * @param wrapper 查询
     * @param data    条件
     */
    void conditionQuery(QueryWrapper<R> wrapper, T data);


}
