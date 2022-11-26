package com.xt.framework.db.mysql.mapper.model;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author tao.xiong
 * @Description 订单表
 * @Date 2022/11/17 13:39
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "t_order", autoResultMap = true)
public class Order extends Model<Order> {
    private static final long serialVersionUID = 5630583943026218557L;
    private Long userId;
    /**
     * sharding 指定自增方案，此处无须指定，否则默认SNOWFLAKE
     */
    @TableId(value = "order_no")
    private Long orderNo;
    private BigDecimal price;
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
        return this.orderNo;
    }
}
