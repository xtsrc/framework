package com.xt.framework.db.mysql.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xt.framwork.common.core.bean.BatchRequest;
import com.xt.framwork.common.core.bean.PageInfo;

import java.io.Serializable;
import java.util.List;

/**
 * @author tao.xiong
 * @Description 通用服务
 * @Date 2021/7/13 17:30
 */
public interface IBaseDao<T, R extends Serializable> extends IService<T> {
    /**
     * 分页查询
     *
     * @param request      批量条件
     * @param queryWrapper 条件查询
     * @return 分页结果
     */
    PageInfo<R> pageQueryDto(BatchRequest<R> request, QueryWrapper<T> queryWrapper);

    /**
     * 查询所有
     *
     * @return 批量结果
     */
    List<R> listAllDto();

    /**
     * 查询单个
     *
     * @param id  主键id
     * @return dto
     */
    R findOneDtoById(Long id);

    /**
     * insert if id is null and code is null
     * update by id or code
     * @param r dto
     * @return 个数
     */
    Long saveOneDto(R r);

}
