package com.xt.framework.db.redis.config;

import com.xt.framework.db.redis.core.RedisUtil;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonQueueConfig {

    @Bean
    public RBlockingQueue<String> blockingQueue() {
        return RedisUtil.getClient().getBlockingQueue("test-block-queue");
    }
    @Bean
    public RDelayedQueue<String> delayedQueue(RBlockingQueue<String> blockQueue) {
        return RedisUtil.getClient().getDelayedQueue(blockQueue);
    }
}
