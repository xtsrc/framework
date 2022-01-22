package com.xt.framework.db.elasticsearch.repositories.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * @author tao.xiong
 * @Description 用户
 * @Date 2022/1/17 18:00
 */
@Document(indexName = "user")
@Data
public class User {
    @Id
    private String uid;
    private String name;
    private Integer age;
    private String address;
}
