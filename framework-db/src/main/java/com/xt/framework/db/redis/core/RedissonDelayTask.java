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
    private RBlockingQueue<String> blockingQueue;

    @PostConstruct
    public void initAsyncListener() {
        // 启动异步监听
        takeAsync();
    }

    private void takeAsync() {
        //非阻塞 IO：立即返回一个 RFuture 对象,底层Netty 的事件循环线程
        blockingQueue.takeAsync().whenComplete((message, throwable) -> {
            if (throwable != null) {
                log.error("Error taking message", throwable);
            } else {
                log.info("Received delayed message: {}", message);
                // 在这里处理消息逻辑
            }
            // 递归调用以继续监听
            takeAsync();
        });
    }
}
