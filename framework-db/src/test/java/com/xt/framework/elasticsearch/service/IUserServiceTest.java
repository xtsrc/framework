package com.xt.framework.elasticsearch.service;

import com.xt.framework.FrameworkDbApplicationTest;
import com.xt.framework.elasticsearch.repositories.model.User;
import com.xt.framework.elasticsearch.repositories.service.IUserService;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * @author tao.xiong
 * @Description es 测试
 * @Date 2022/1/18 17:02
 */
public class IUserServiceTest extends FrameworkDbApplicationTest {
    @Resource
    private IUserService userService;

    @Test
    public void testSave() {
        User user=new User();
        user.setUid("xt-01");
        user.setName("测试");
        user.setAddress("测试地址");
        user.setAge(20);
        userService.save(user);
    }

    public void testDelete() {
    }

    public void testPageQuery() {
    }
}