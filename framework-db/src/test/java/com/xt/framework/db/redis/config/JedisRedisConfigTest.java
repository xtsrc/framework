package com.xt.framework.db.redis.config;

import com.xt.framework.db.FrameworkDbApplicationTest;
import com.xt.framework.db.redis.util.RedisUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;

/**
 * @author tao.xiong
 * @Description redis 测试
 * @Date 2022/1/18 15:51
 */
public class JedisRedisConfigTest extends FrameworkDbApplicationTest {

    @Test
    public void query() {
        RedisUtil.del("strKey");
        RedisUtil.set("strKey","11");
        System.out.println(RedisUtil.get("strKey"));
    }

}