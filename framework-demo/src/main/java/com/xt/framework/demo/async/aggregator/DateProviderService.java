package com.xt.framework.demo.async.aggregator;

import com.google.common.collect.Lists;
import io.github.lvyahui8.spring.annotation.DataProvider;
import io.github.lvyahui8.spring.annotation.InvokeParameter;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author tao.xiong
 * @date 2023/4/11 17:17
 * @desc 数据提供
 */
@Service
public class DateProviderService {
    @DataProvider(id = "user")
    public User get(@InvokeParameter("userId") Long id) {
        User user = new User();
        user.setUserId(id);
        user.setName("test");
        return user;
    }

    @DataProvider(id = "posts")
    public List<Post> getPosts(@InvokeParameter("userId") Long userId) {
        List<Post> posts = Lists.newArrayList();
        Post post = new Post();
        post.setName("post"+userId);
        posts.add(post);
        return posts;
    }
    @DataProvider(id = "followers")
    public List<Follower> getFollowers(@InvokeParameter("userId") Long userId) {
        List<Follower> followers = Lists.newArrayList();
        Follower follower = new Follower();
        follower.setName("follower"+userId);
        followers.add(follower);
        return followers;
    }
}
