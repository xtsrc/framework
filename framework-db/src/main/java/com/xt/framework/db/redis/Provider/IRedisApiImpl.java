package com.xt.framework.db.redis.Provider;

import com.xt.framework.db.api.IRedisApi;
import com.xt.framework.db.redis.core.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RequestMapping("/redis/api")
@RestController
@Slf4j
public class IRedisApiImpl implements IRedisApi {

    @Override
    public Boolean setIfAbsent(String key, Object value, Long minutes) {
        return RedisUtil.inst().opsForValue().setIfAbsent(key,value,minutes, TimeUnit.MINUTES);
    }
}
