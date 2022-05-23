package com.xt.framework.db.mysql.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xt.framework.db.FrameworkDbApplicationTest;
import com.xt.framework.db.mysql.model.SyAdmin;
import com.xt.framework.db.mysql.service.ISyAdminService;
import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.function.Consumer;

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
        syAdminService.dealWithStream(Wrappers.<SyAdmin>lambdaQuery().eq(SyAdmin::getId,8), System.out::println);
    }
}