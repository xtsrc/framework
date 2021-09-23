package com.xt.framework.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xt.framework.common.core.bean.BatchRequest;
import com.xt.framework.common.core.bean.PageInfo;
import com.xt.framework.common.core.dao.impl.BaseDaoImpl;
import com.xt.framework.dto.UserDto;
import com.xt.framework.mapper.UserMapper;
import com.xt.framework.model.User;
import com.xt.framework.service.IUserService;
import org.springframework.stereotype.Service;

/**
 * @author tao.xiong
 * @Description
 * @Date 2021/7/28 17:50
 */
@Service
public class UserServiceImpl extends BaseDaoImpl<UserMapper, User, UserDto> implements IUserService {

    @Override
    public PageInfo<UserDto> pageQuery(BatchRequest<UserDto> queryParam) {
        return pageQueryDto(queryParam, this.conditionQuery(queryParam.getData()));
    }

    protected QueryWrapper<User> conditionQuery(UserDto data) {
        QueryWrapper<User> queryWrapper = Wrappers.query();
        if (data.getId() != null) {
            queryWrapper.lambda().and(obj -> obj.eq(User::getId, data.getId()));
        } else {
            queryWrapper.lambda().and(data.getName() != null, obj -> obj.like(User::getName, data.getName()));
        }
        return queryWrapper;
    }
}
