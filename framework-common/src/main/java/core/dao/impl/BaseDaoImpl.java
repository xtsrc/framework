package core.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import core.bean.BatchRequest;
import core.bean.PageInfo;
import core.dao.IBaseDao;
import core.util.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

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
public class BaseDaoImpl<M extends BaseMapper<T>, T, R> extends ServiceImpl<M, T> implements IBaseDao<T, R> {
    protected Class<R> dtoClass = currentDtoClass();
    protected Class<T> modelClass = currentModelClass();

    @Override
    public PageInfo<R> pageQueryDto(BatchRequest<R> request, QueryWrapper<T> queryWrapper) {
        QueryWrapper<T> wrapper = getCommonQueryWrapper(request, queryWrapper);
        IPage<T> page = new Page<>(request.getPage(), request.getPageSize());
        page = baseMapper.selectPage(page, wrapper);
        return new PageInfo<>((int) page.getTotal(), BeanUtil.copyListProperties(page.getRecords(), dtoClass));
    }

    @Override
    public List<R> listAllDto() {
        return  BeanUtil.copyListProperties(baseMapper.selectList(Wrappers.emptyWrapper()), dtoClass);
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
    public T findOneByCode(String code) {
        return baseMapper.selectOne(Wrappers.<T>query().eq("code",code));
    }

    @Override
    public Long saveOneDto(R r) {
        log.info("save one :{}", r);
        T t = BeanUtils.instantiateClass(modelClass);
        BeanUtils.copyProperties(r, t);
        Object id;
        try {
            id = Objects.requireNonNull(BeanUtils.findDeclaredMethodWithMinimalParameters(dtoClass, "getId")).invoke(r);
            if (id == null) {
                Method setCodeMethod = BeanUtils.findDeclaredMethodWithMinimalParameters(modelClass, "setCode");
                if (setCodeMethod != null) {
                    setCodeMethod.invoke(t, UUID.randomUUID().toString());
                }
                baseMapper.insert(t);
            } else {
                baseMapper.updateById(t);
            }
        } catch (Exception e) {
            log.error("save one dto error :{}", e.getMessage());
            return 0L;
        }
        return (Long) id;
    }

    private QueryWrapper<T> getCommonQueryWrapper(BatchRequest<R> request, QueryWrapper<T> queryWrapper) {
        if (request != null) {
            queryWrapper.orderBy(StringUtils.isNotBlank(request.getSortField()),
                    StringUtils.equalsIgnoreCase("ASC", request.getSortOrder()), request.getSortField())
                    .and(!StringUtils.isAllBlank(request.getIncludeFields()), obj -> obj.select(request.getIncludeFields()))
                    .ge(request.getCreateStartDate() != null, "create_time", request.getCreateStartDate())
                    .le(request.getCreateEndDate() != null, "update_time", request.getCreateEndDate());
        }
        return queryWrapper;
    }

    protected Class<R> currentDtoClass() {
        return (Class<R>) ReflectionKit.getSuperClassGenericType(getClass(), 2);
    }
}
