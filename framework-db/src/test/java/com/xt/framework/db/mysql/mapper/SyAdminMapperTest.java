package com.xt.framework.db.mysql.mapper;


import com.google.common.collect.Lists;
import com.xt.framework.db.FrameworkDbApplicationTest;
import com.xt.framework.db.mysql.model.SyAdmin;
import com.xt.framwork.common.core.bean.DictInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.annotation.Resource;
@Slf4j
public class SyAdminMapperTest extends FrameworkDbApplicationTest {
    @Resource
    private SyAdminMapper syAdminMapper;
    @Test
    public void query(){
        SyAdmin syAdmin=syAdminMapper.selectById(8);
        System.out.println(syAdmin);
    }
    @Test
    public void del(){
        syAdminMapper.deleteById(5);
    }
    @Test
    public void insert(){
        SyAdmin syAdmin=new SyAdmin();
        syAdmin.setChannelCode("CSQD");
        syAdmin.setChannelDiscount(9.0f);
        syAdmin.setName("熊涛");
        syAdmin.setPhone("18811010745");
        syAdmin.setStaffAmount(100);
        syAdmin.setUserId(1L);
        DictInfo dictInfo=new DictInfo();
        dictInfo.setKey("测试1");
        dictInfo.setValue("测试2");
        syAdmin.setRemark(dictInfo);
        syAdmin.setShareUrl(Lists.newArrayList(dictInfo));
        syAdmin.setCreateBy(Lists.newArrayList("xt1","xt2"));
        syAdmin.setUpdateBy(Lists.newArrayList("xt3","xt4"));
        syAdminMapper.insert(syAdmin);
    }
}