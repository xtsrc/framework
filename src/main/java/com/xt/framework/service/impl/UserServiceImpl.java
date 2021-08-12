package com.xt.framework.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xt.framework.common.core.bean.PageInfo;
import com.xt.framework.common.core.dao.IBatchDao;
import com.xt.framework.dto.UserQueryParam;
import com.xt.framework.mapper.UserMapper;
import com.xt.framework.model.User;
import com.xt.framework.service.IUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author tao.xiong
 * @Description
 * @Date 2021/7/28 17:50
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Resource
    private IBatchDao<UserQueryParam, User> batchDao;

    @Override
    public PageInfo<User> pageQuery(UserQueryParam queryParam) {
        return batchDao.pageQuery(queryParam, this::conditionQuery, baseMapper);
    }

    protected void conditionQuery(QueryWrapper<User> wrapper, UserQueryParam data) {
        wrapper.lambda().and(data.getUser() != null, obj -> obj.eq(User::getUserId, data.getUser()));
    }
}
