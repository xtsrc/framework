package com.xt.framework.db.elasticsearch.repositories.service.impl;

import com.xt.framework.db.elasticsearch.repositories.dao.UserRepository;
import com.xt.framework.db.elasticsearch.repositories.model.User;
import com.xt.framework.db.elasticsearch.repositories.service.IUserService;
import com.xt.framwork.common.core.bean.BatchRequest;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author tao.xiong
 * @Description 实现类
 * @Date 2022/1/17 18:16
 */
@Service
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
