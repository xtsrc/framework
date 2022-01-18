package com.xt.framework.mysql.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xt.framework.mysql.service.IBaseDao;
import com.xt.framwork.core.bean.BatchRequest;
import com.xt.framwork.core.bean.PageInfo;
import com.xt.framwork.core.util.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author tao.xiong
 * @Description 基础服务
 * @Date 2021/7/13 17:50
 */
@Slf4j
public class BaseDaoImpl<M extends BaseMapper<T>, T, R extends Serializable> extends ServiceImpl<M, T> implements IBaseDao<T, R> {
    protected Class<R> dtoClass = currentDtoClass();
    protected Class<T> modelClass = currentModelClass();

    @Override
    public PageInfo<R> pageQueryDto(BatchRequest<R> request, QueryWrapper<T> queryWrapper) {
        if (request == null) {
            return new PageInfo<>();
        }
        QueryWrapper<T> wrapper = getCommonQueryWrapper(request, queryWrapper);
        IPage<T> page = new Page<>(request.getPage(), request.getPageSize());
        page = baseMapper.selectPage(page, wrapper);
        return new PageInfo<>((int) page.getTotal(), BeanUtil.copyListProperties(page.getRecords(), dtoClass));
    }

    @Override
    public List<R> listAllDto() {
        return BeanUtil.copyListProperties(baseMapper.selectList(Wrappers.emptyWrapper()), dtoClass);
    }

    @Override
    public R findOneDtoById(Long id) {
        T t = baseMapper.selectById(id);
        if (t == null) {
            return null;
        }
        R r = BeanUtils.instantiateClass(dtoClass);
        BeanUtils.copyProperties(t, r);
        return r;
    }

    @Override
    public Long saveOneDto(R r) {
        log.info("save one :{}", r);
        T t = BeanUtils.instantiateClass(modelClass);
        BeanUtils.copyProperties(r, t);
        Object id;
        long i;
        try {
            id = Objects.requireNonNull(BeanUtils.findDeclaredMethodWithMinimalParameters(dtoClass, "getId")).invoke(r);
            if (id == null) {
                Method getCodeMethod = BeanUtils.findDeclaredMethodWithMinimalParameters(modelClass, "getCode");
                if (getCodeMethod != null) {
                    Object code = getCodeMethod.invoke(t);
                    if (code != null) {
                        i = baseMapper.update(t, Wrappers.<T>update().eq("code", code));
                        if (i == 0) {
                            i = baseMapper.insert(t);
                        }
                    } else {
                        Objects.requireNonNull(BeanUtils.findDeclaredMethodWithMinimalParameters(modelClass, "setCode"))
                                .invoke(t, UUID.randomUUID());
                        i = baseMapper.insert(t);
                    }
                } else {
                    i = baseMapper.insert(t);
                }
            } else {
                i = baseMapper.updateById(t);
            }
        } catch (Exception e) {
            log.error("save one dto error :{}", e.getMessage());
            return 0L;
        }
        return i;
    }

    private QueryWrapper<T> getCommonQueryWrapper(BatchRequest<R> request, QueryWrapper<T> queryWrapper) {
        if (request != null) {
            queryWrapper.orderBy(StringUtils.isNotBlank(request.getSortField()),
                    StringUtils.equalsIgnoreCase("ASC", request.getSortOrder()), request.getSortField())
                    .and(!StringUtils.isAllBlank(request.getIncludeFields()), obj -> obj.select(request.getIncludeFields()))
                    .ge(request.getStartDate() != null, "update_time", request.getStartDate())
                    .le(request.getEndDate() != null, "update_time", request.getEndDate());
        }
        return queryWrapper;
    }

    protected Class<R> currentDtoClass() {
        return (Class<R>) ReflectionKit.getSuperClassGenericType(getClass(), 2);
    }
}
