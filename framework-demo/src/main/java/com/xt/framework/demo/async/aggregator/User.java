package com.xt.framework.demo.async.aggregator;

import lombok.Data;

import java.util.List;

/**
 * @author tao.xiong
 * @date 2023/4/11 18:04
 * @desc
 */
@Data
public class User {
    private String name;
    private Long userId;
    private List<Post> posts;
    private List<Follower> followers;
}
