package com.xt.framework.db.redis.core;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
@Slf4j
public class RedissonDelayTask {
    @Resource
    private RBlockingQueue<Object> blockingQueue;

    @PostConstruct
    public void take() {
        new Thread(() -> {
            while (true) {
                try {
                    //将到期的数据取出来，如果一直没有到期数据，就一直等待。
                    log.info(blockingQueue.take().toString());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
