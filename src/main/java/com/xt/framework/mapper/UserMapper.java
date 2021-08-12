package com.xt.framework.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xt.framework.model.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author tao.xiong
 * @Description
 * @Date 2021/7/28 17:45
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
