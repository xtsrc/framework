package com.xt.framework.demo.async.aggregator;

import com.alibaba.fastjson.JSON;
import com.xt.framework.demo.FrameworkDemoApplicationTest;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * @author tao.xiong
 * @date 2023/4/11 18:21
 * @desc TODO
 */
public class DataServiceTest extends FrameworkDemoApplicationTest {
    @Resource
    DataService dataService;

    @Test
    public void query() {
        User user = dataService.query(1L);
        System.out.println(JSON.toJSONString(user));
    }
}