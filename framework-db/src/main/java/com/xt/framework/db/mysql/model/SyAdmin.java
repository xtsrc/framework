package com.xt.framework.db.mysql.model;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.xt.framework.db.handler.EncryptionTypeHandler;
import com.xt.framework.db.handler.JsonArrayBaseHandler;
import com.xt.framework.db.handler.JsonArrayHandler;
import com.xt.framwork.common.core.bean.DictInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import static com.baomidou.mybatisplus.annotation.FieldStrategy.NOT_NULL;

/**
 * <p>
 * 私域管理员表
 * </p>
 *
 * @author tao.xiong
 * @since 2022-01-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "t_sy_admin",autoResultMap=true)
public class SyAdmin extends Model<SyAdmin> {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 会员中心员工id
     */
    private Long userId;

    /**
     * 管理员名称
     */
    private String name;

    /**
     * 电话号码
     */
    @TableField(value = "phone", typeHandler = EncryptionTypeHandler.class)
    private String phone;

    /**
     * 渠道编码
     */
    private String channelCode;

    /**
     * 渠道折扣
     */
    private Float channelDiscount;

    /**
     * 员工数量
     */
    private Integer staffAmount;

    /**
     * 备注
     */
    @TableField(jdbcType = JdbcType.VARCHAR, insertStrategy = NOT_NULL, typeHandler = FastjsonTypeHandler.class)
    private DictInfo remark;

    @TableField(jdbcType = JdbcType.VARCHAR, insertStrategy = NOT_NULL, typeHandler = JsonArrayHandler.ListDictHandler.class)
    private List<DictInfo> shareUrl;

    /**
     * 状态(0:禁用 1:启用)
     */
    private Boolean status;

    /**
     * 是否删除(0:正常 1:已删除)
     */
    private Boolean isDeleted;

    /**
     * 创建人
     */
    @TableField(jdbcType = JdbcType.VARCHAR, insertStrategy = NOT_NULL, typeHandler = JsonArrayBaseHandler.ListStringBaseHandler.class)
    private List<String> createBy;

    /**
     * 更新人
     */
    @TableField(jdbcType = JdbcType.VARCHAR, insertStrategy = NOT_NULL, typeHandler = JsonArrayHandler.ListStringHandler.class)
    private List<String> updateBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
