package com.xt.framework.db.mysql.model;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.xt.framework.db.handler.EncryptionTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 日志实体类
 *
 * @author tao.xiong
 */
@EqualsAndHashCode(callSuper = false)
@TableName(value = "t_log", autoResultMap = true)
@Data
public class Log extends Model<Log> {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     *
     */
    private static final long serialVersionUID = 4296799309713867875L;
    /**
     * 请求唯一标识，可以自定义规则生成
     */
    private String traceId;
    /**
     * 请求路径
     */
    private String uri;
    /**
     * 请求url上的参数
     */
    private String queryString;
    /**
     * 请求方式，GET/POST等
     */
    private String method;
    /**
     * 操作说明
     */
    private String description;
    /**
     * 请求ip，客户端ip地址
     */
    private String ip;
    /**
     * 请求体，请求正文的内容
     */
    private String body;
    /**
     * 请求token，登录用户token，登录状态下存在
     */
    @TableField(value = "token", typeHandler = EncryptionTypeHandler.class)
    private String token;
    /**
     * 请求用户id，登录用户id，登录状态下存在
     */
    private Long userId;
    /**
     * 返回结果，请求的结果
     */
    private String returnData;
    private long startTime;
    private long endTime;
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
