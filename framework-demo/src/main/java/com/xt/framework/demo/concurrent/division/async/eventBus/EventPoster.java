package com.xt.framework.demo.concurrent.division.async.eventBus;

import com.google.common.eventbus.AsyncEventBus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author tao.xiong
 * @date 2023/4/12 15:06
 * @desc
 */
@Service
public class EventPoster {
    @Resource
    private AsyncEventBus asyncEventBus;

    public void post(String name) {
        Event event = new Event();
        event.setName(name);
        asyncEventBus.post(event);
    }

}
