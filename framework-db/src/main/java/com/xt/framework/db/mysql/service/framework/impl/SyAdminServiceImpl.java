package com.xt.framework.db.mysql.service.framework.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xt.framework.db.mysql.mapper.framework.SyAdminMapper;
import com.xt.framework.db.mysql.mapper.framework.model.SyAdmin;
import com.xt.framework.db.mysql.service.framework.ISyAdminService;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

/**
 * <p>
 * 私域管理员表 服务实现类
 * </p>
 *
 * @author tao.xiong
 * @since 2022-01-12
 */
@Service
public class SyAdminServiceImpl extends ServiceImpl<SyAdminMapper, SyAdmin> implements ISyAdminService {

    @Override
    public void dealWithStream(LambdaQueryWrapper<SyAdmin> queryWrapper, Consumer<SyAdmin> consumer) {
        baseMapper.dealWithStream(queryWrapper, resultContext -> consumer.accept(resultContext.getResultObject()));
    }
}
