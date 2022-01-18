package com.xt.framework.elasticsearch.dao;

import com.xt.framework.elasticsearch.model.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author tao.xiong
 * @Description dao
 * @Date 2022/1/17 18:10
 */
@Repository
public interface UserRepository extends ElasticsearchRepository<User, String> {
}
