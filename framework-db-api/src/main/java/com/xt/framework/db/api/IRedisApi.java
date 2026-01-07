package com.xt.framework.db.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(value = "framework-db", url = "${xt.server.api.framework-db:}", path = "/redis/api", contextId = "redis")
public interface IRedisApi {
    @PostMapping("/setIfAbsent")
    Boolean setIfAbsent(@RequestParam String key, @RequestParam Object value, @RequestParam Long minutes);
}
