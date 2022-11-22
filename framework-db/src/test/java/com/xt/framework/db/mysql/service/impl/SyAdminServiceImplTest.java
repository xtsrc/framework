package com.xt.framework.db.mysql.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xt.framework.db.FrameworkDbApplicationTest;
import com.xt.framework.db.mysql.mapper.framework.model.SyAdmin;
import com.xt.framework.db.mysql.service.framework.ISyAdminService;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author tao.xiong
 * @Description mysql 测试
 * @Date 2022/5/23 10:54
 */
@SpringBootTest
public class SyAdminServiceImplTest extends FrameworkDbApplicationTest {

    @Resource
    private ISyAdminService syAdminService;

    @Test
    public void testDealWithStream() {
        syAdminService.dealWithStream(Wrappers.<SyAdmin>lambdaQuery().eq(SyAdmin::getId, 8), System.out::println);
    }
}