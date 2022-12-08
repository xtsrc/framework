package com.xt.framework.db.mysql.service.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.xt.framework.db.cache.HotData;
import com.xt.framework.db.mysql.handler.EncryptionTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author tao.xiong
 * @Description dto
 * @Date 2022/11/26 16:23
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SyAdminInfo extends HotData {
    private static final long serialVersionUID = -2268367551020893308L;
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

    private String shareUrl;

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
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
