package com.xt.framework.elasticsearch.service.impl;

import com.xt.framework.elasticsearch.dao.UserRepository;
import com.xt.framework.elasticsearch.model.User;
import com.xt.framework.elasticsearch.service.IUserService;
import com.xt.framwork.core.bean.BatchRequest;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import javax.annotation.Resource;

/**
 * @author tao.xiong
 * @Description 实现类
 * @Date 2022/1/17 18:16
 */
public class UserServiceImpl implements IUserService {
    @Resource
    private UserRepository userRepository;

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    public Page<User> pageQuery(BatchRequest<User> batchRequest) {
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchPhraseQuery("name", batchRequest.getData()))
                .withPageable(PageRequest.of(Math.toIntExact(batchRequest.getPage()), batchRequest.getPageSize()))
                .build();
        return userRepository.search(searchQuery);
    }
}
