package com.xt.framework.db.mysql.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.xt.framework.db.mysql.mapper.model.SyAdmin;
import com.xt.framework.db.mysql.service.dto.SyAdminInfo;
import org.apache.ibatis.annotations.*;
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
     * xml自己写的sql 需要自定义resultMap 或者使用 @ResultMap("mybatis-plus_实体类名")
     * @param wrapper 查询
     * @param handler 处理
     */
    @ResultMap("mybatis-plus_SyAdmin")
    @Select("select * from t_sy_admin t ${ew.customSqlSegment}")
    @Options(resultSetType = ResultSetType.FORWARD_ONLY, fetchSize = 1000)
    void dealWithStream(@Param(Constants.WRAPPER) LambdaQueryWrapper<SyAdmin> wrapper, ResultHandler<SyAdmin> handler);

    /**
     * 常用查询
     * @param syAdminInfo 条件
     * @return 结果
     */
    SyAdmin query(@Param("queryParam") SyAdminInfo syAdminInfo);

}
