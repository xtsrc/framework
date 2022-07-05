package com.xt.framework.db.redis.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author tao.xiong
 * @Description lettuce 客户端
 * @Date 2022/7/5 17:23
 */
@Configuration
public class LettuceConfig {
    @Bean
    public RedisTemplate<String, Object> lettuceTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        //配置序列化方式
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //使用fastjson 代替 GenericJackson2JsonRedisSerializer
        redisTemplate.setValueSerializer(new GenericFastJsonRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericFastJsonRedisSerializer());
        return redisTemplate;
    }
}
