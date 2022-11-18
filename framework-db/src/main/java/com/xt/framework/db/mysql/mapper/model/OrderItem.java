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
 * @Description 订单明细表
 * @Date 2022/11/17 13:39
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "t_order_item", autoResultMap = true)
public class OrderItem extends Model<OrderItem> {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private String orderNo;
    private String itemName;
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
        return this.id;
    }
}
