package com.xt.framework.demo.concurrent.division.async.eventBus;

import com.google.common.eventbus.Subscribe;
import org.springframework.stereotype.Component;

/**
 * @author tao.xiong
 * @date 2023/4/12 15:01
 * @desc 监听
 */
@Component
public class EventListener {
    @Subscribe
    public void listener(Event event) {
        System.out.println("监听事件："+event);
    }
}
