package com.xt.framework.db.mysql.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xt.framework.db.mysql.model.SyAdmin;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.function.Consumer;

/**
 * <p>
 * 私域管理员表 服务类
 * </p>
 *
 * @author tao.xiong
 * @since 2022-01-12
 */
public interface ISyAdminService extends IService<SyAdmin> {
    /**
     * 流式处理
     * @param queryWrapper 查询
     * @param consumer handler
     */
    void dealWithStream(QueryWrapper<SyAdmin> queryWrapper, Consumer<SyAdmin> consumer);
}
