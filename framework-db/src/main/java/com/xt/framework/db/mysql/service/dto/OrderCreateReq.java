package com.xt.framework.db.mysql.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author tao.xiong
 * @Description 订单新增请求
 * @Date 2022/11/22 18:26
 */
@Data
public class OrderCreateReq implements Serializable {
    private static final long serialVersionUID = 2682679703952239520L;
    private Long userId;
    private Long orderNo;
    private BigDecimal price;
    private List<Item> items;

    @Data
    public static class Item implements Serializable {
        private static final long serialVersionUID = 700755422933326861L;
        private BigDecimal price;
        private String itemName;
        private Long itemNo;
    }
}
