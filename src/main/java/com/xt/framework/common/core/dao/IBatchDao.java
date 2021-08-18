package com.xt.framework.common.core.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.xt.framework.common.core.bean.PageInfo;
import com.xt.framework.common.core.bean.BatchRequest;

import java.util.List;

/**
 * @author tao.xiong
 * @Description 通用服务
 * @Date 2021/7/13 17:30
 */
public interface IBatchDao<R extends BatchRequest<R>, M extends Model<M>> {
    /**
     * 分页查询
     *
     * @param request    批量条件
     * @param query      条件查询回调
     * @param baseMapper 注入mapper
     * @return 分页结果
     * @Description 注入非唯一bean:继承和实现注入泛型时才能获得具体类型
     */
    PageInfo<M> pageQuery(R request, QueryCallBack<M, R> query, BaseMapper<M> baseMapper);

    /**
     * 查询所有
     *
     * @param request    批量条件
     * @param query      条件查询回调
     * @param baseMapper 注入mapper
     * @return 批量结果
     */
    List<M> listAll(R request, QueryCallBack<M, R> query, BaseMapper<M> baseMapper);

    /**
     * 查询单个
     *
     * @param request    单个条件
     * @param query      条件查询回调
     * @param baseMapper 注入mapper
     * @return 单个结果
     */
    M findOne(R request, QueryCallBack<M, R> query, BaseMapper<M> baseMapper);
}
