package com.xt.framework.service;

import com.xt.framework.common.core.bean.BatchRequest;
import com.xt.framework.common.core.bean.PageInfo;
import com.xt.framework.common.core.dao.IBaseDao;
import com.xt.framework.dto.UserDto;
import com.xt.framework.model.User;

/**
 * @author tao.xiong
 * @Description
 * @Date 2021/7/28 17:48
 */

public interface IUserService extends IBaseDao<User, UserDto> {
    /**
     * 分页查询
     *
     * @param queryParam 请求
     * @return 分页返回
     */
    PageInfo<UserDto> pageQuery(BatchRequest<UserDto> queryParam);
}
