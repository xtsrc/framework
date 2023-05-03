package com.xt.framework.demo.async.eventBus;

import com.xt.framework.demo.FrameworkDemoApplicationTest;
import org.junit.Test;

import javax.annotation.Resource;

import static org.junit.Assert.*;

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