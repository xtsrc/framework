package com.xt.framework.db.mysql.mapper;


import com.xt.framework.db.FrameworkDbApplicationTest;
import org.junit.Test;

import javax.annotation.Resource;

public class SyAdminMapperTest extends FrameworkDbApplicationTest {
    @Resource
    private SyAdminMapper syAdminMapper;
    @Test
    public void query(){
        System.out.println(syAdminMapper.selectById(1));
    }
}