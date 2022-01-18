package com.xt.framework.redis.config;

import com.xt.framework.FrameworkDbApplicationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author tao.xiong
 * @Description redis 测试
 * @Date 2022/1/18 15:51
 */
public class JedisRedisConfigTest extends FrameworkDbApplicationTest {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    public void query() {
        redisTemplate.opsForValue().set("strKey", "zwqh");
        System.out.println(redisTemplate.opsForValue().get("strKey"));
    }

}