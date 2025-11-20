package com.xt.framework.demo.async.eventBus;

import com.xt.framework.demo.FrameworkDemoApplicationTest;
import com.xt.framework.demo.concurrent.division.async.eventBus.EventPoster;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * @author tao.xiong
 * @date 2023/4/12 15:39
 * @desc TODO
 */
public class EventPosterTest extends FrameworkDemoApplicationTest {
    @Resource
    private EventPoster eventPoster;
    @Test
    public void post() {
        eventPoster.post("test");
    }
}