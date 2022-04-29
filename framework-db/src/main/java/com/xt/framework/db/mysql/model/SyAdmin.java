package com.xt.framework.db.mysql.model;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.xt.framwork.common.core.handler.EncryptionTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

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
@TableName("t_sy_admin")
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
    private String remark;

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
    private String createBy;

    /**
     * 更新人
     */
    private String updateBy;

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
