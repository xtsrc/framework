package com.xt.framework.common.core.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xt.framework.common.core.bean.PageInfo;
import com.xt.framework.common.core.dao.IBatchDao;
import com.xt.framework.common.core.dao.QueryCallBack;
import com.xt.framework.common.core.bean.BatchRequest;
import org.bouncycastle.util.Arrays;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;
import java.util.List;

/**
 * @author tao.xiong
 * @Description 基础服务
 * @Date 2021/7/13 17:50
 */
@Service
public class BatchDaoImpl<T extends BatchRequest<T>, M extends Model<M>> implements IBatchDao<T, M> {

    @Override
    public PageInfo<M> pageQuery(T request, QueryCallBack<M, T> query, BaseMapper<M> baseMapper) {
        QueryWrapper<M> wrapper = getCommonQueryWrapper(request, query);
        IPage<M> page = new Page<>(request.getPage(), request.getPageSize());
        page = baseMapper.selectPage(page, wrapper);
        return new PageInfo<>((int) page.getTotal(), page.getRecords());
    }

    @Override
    public List<M> listAll(T request, QueryCallBack<M, T> query, BaseMapper<M> baseMapper) {
        QueryWrapper<M> wrapper = getCommonQueryWrapper(request, query);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public M findOne(T request, QueryCallBack<M, T> query, BaseMapper<M> baseMapper) {
        QueryWrapper<M> wrapper = getCommonQueryWrapper(request,query);
        return baseMapper.selectList(wrapper).stream().findFirst().orElse(null);
    }

    private QueryWrapper<M> getCommonQueryWrapper(T request, QueryCallBack<M, T> query) {
        QueryWrapper<M> wrapper = Wrappers.<M>query().orderBy(StringUtils.isNotBlank(request.getSortField()),
                StringUtils.equalsIgnoreCase("ASC", request.getSortOrder()), request.getSortField())
                .and(!Arrays.isNullOrContainsNull(request.getIncludeFields()), obj -> obj.select(request.getIncludeFields()))
                .ge(request.getCreateStartDate() != null, "create_time", request.getCreateStartDate())
                .le(request.getCreateEndDate() != null, "create_time", request.getCreateEndDate());
        query.conditionQuery(wrapper, request);
        return wrapper;
    }
}
