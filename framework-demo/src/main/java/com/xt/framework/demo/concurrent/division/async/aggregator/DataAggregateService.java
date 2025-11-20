package com.xt.framework.demo.concurrent.division.async.aggregator;

import io.github.lvyahui8.spring.annotation.DataConsumer;
import io.github.lvyahui8.spring.annotation.DataProvider;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author tao.xiong
 * @date 2023/4/11 18:03
 * @desc 异步数据聚合
 */
@Component
public class DataAggregateService {
    @DataProvider(id = "userFullData")
    public User userFullData(@DataConsumer(id = "user") User user,
                             @DataConsumer(id = "posts") List<Post> posts,
                             @DataConsumer(id = "followers") List<Follower> followers) {
        user.setFollowers(followers);
        user.setPosts(posts);
        return user;
    }
}
