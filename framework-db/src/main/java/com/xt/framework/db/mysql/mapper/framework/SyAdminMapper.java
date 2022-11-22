package com.xt.framework.db.mysql.mapper.framework;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.xt.framework.db.mysql.mapper.framework.model.SyAdmin;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.session.ResultHandler;

/**
 * <p>
 * 私域管理员表 Mapper 接口
 * </p>
 *
 * @author tao.xiong
 * @since 2022-01-12
 */
public interface SyAdminMapper extends BaseMapper<SyAdmin> {
    /**
     * 流式处理
     *
     * @param wrapper 查询
     * @param handler 处理
     */
    @Select("select * from t_sy_admin t ${ew.customSqlSegment}")
    @Options(resultSetType = ResultSetType.FORWARD_ONLY, fetchSize = 1000)
    @ResultType(SyAdmin.class)
    void dealWithStream(@Param(Constants.WRAPPER) LambdaQueryWrapper<SyAdmin> wrapper, ResultHandler<SyAdmin> handler);

    /**
     * 常用查询
     * @param syAdmin 条件
     * @return 结果
     */
    SyAdmin query(@Param("queryParam")SyAdmin syAdmin);

}
