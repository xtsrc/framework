package com.xt.framework.db.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@FeignClient(value = "framework-db", url = "${xt.server.api.framework-db:}",path = "/redis/api",contextId = "redis")
public interface IRedisApi {
    RedisTemplate<String, Object> inst();
}
