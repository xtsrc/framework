package com.xt.framework.elasticsearch.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * @author tao.xiong
 * @Description 用户
 * @Date 2022/1/17 18:00
 */
@Document(indexName = "user")
public class User {
    @Id
    private String uid;
    private String name;
    private Integer age;
    private String address;
}
