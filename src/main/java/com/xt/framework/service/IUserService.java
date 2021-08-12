package com.xt.framework.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xt.framework.common.core.bean.PageInfo;
import com.xt.framework.dto.UserQueryParam;
import com.xt.framework.model.User;

/**
 * @author tao.xiong
 * @Description
 * @Date 2021/7/28 17:48
 */

public interface IUserService extends IService<User> {
    /**
     * 分页查询
     * @param queryParam 请求
     * @return 分页返回
     */
    PageInfo<User> pageQuery(UserQueryParam queryParam);
}
