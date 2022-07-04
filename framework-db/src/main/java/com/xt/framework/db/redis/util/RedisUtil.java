package com.xt.framework.db.redis.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * @author tao.xiong
 * @Description redis 工具
 * @Date 2022/5/9 11:12
 */
@Slf4j
public class RedisUtil {
    @Resource
    protected static final RedisTemplate<String, Object> REDIS_TEMPLATE = SpringBeanUtil.getBean("redisTemplate");
    private static final String LOCK_KEY = "redis_lock";
    private static final long EXPIRE_TIME = 5;

    public static RedisTemplate<String, Object> inst() {
        return REDIS_TEMPLATE;
    }

    private RedisUtil() {
        throw new IllegalStateException("utility class");
    }

    /**
     * 加锁，无阻塞
     *
     * @param key 加锁id
     * @return 加锁结果
     */
    public static Boolean tryLock(String key) {
        try {
            //SET命令返回OK ，则证明获取锁成功
            assert REDIS_TEMPLATE != null;
            return REDIS_TEMPLATE.opsForValue().setIfAbsent(LOCK_KEY + key, 1, EXPIRE_TIME, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 解锁
     *
     * @param key 解锁id
     * @return 解锁结果
     */
    public static Boolean unlock(String key) {
        String success = "1";
        try {
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            RedisScript<String> redisScript = new DefaultRedisScript<>(script, String.class);
            //redis脚本执行
            assert REDIS_TEMPLATE != null;
            Object result = REDIS_TEMPLATE.execute(redisScript, Collections.singletonList(LOCK_KEY + key));
            if (success.equals(result)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
