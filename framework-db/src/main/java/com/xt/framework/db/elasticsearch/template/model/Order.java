package com.xt.framework.db.elasticsearch.template.model;

import lombok.Data;

import java.util.Date;
import java.util.List;
@Data
public class Order {
    private int status;
    private String no;
    private Date createTime;
    private double amount;
    private String creator;
    private List<Product> product;
}
