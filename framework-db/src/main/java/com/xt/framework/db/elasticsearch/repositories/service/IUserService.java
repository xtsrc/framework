package com.xt.framework.db.elasticsearch.repositories.service;

import com.xt.framework.db.elasticsearch.repositories.model.User;
import com.xt.framwork.common.core.bean.BatchRequest;
import org.springframework.data.domain.Page;

/**
 * @author tao.xiong
 * @Description 服务层
 * @Date 2022/1/17 18:12
 */
public interface IUserService {
    /**
     * 保存
     *
     * @param user entity
     * @return save entity
     */
    User save(User user);

    /**
     * 删除
     *
     * @param user entity
     */
    void delete(User user);

    /**
     * 分页查询
     *
     * @param batchRequest 批量请求
     * @return 分页结果
     */
    Page<User> pageQuery(BatchRequest<User> batchRequest);
}
