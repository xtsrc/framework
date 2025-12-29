package com.xt.framework.db.redis.Provider;

import com.xt.framework.db.api.IRedisApi;
import com.xt.framework.db.redis.core.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/redis/api")
@RestController
@Slf4j
public class IRedisApiImpl implements IRedisApi {

    @Override
    public RedisTemplate<String, Object> inst() {
        return RedisUtil.inst();
    }
}
