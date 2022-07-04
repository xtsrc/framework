package com.xt.framework.db.redis.config;

import com.xt.framework.db.FrameworkDbApplicationTest;
import com.xt.framework.db.redis.util.RedisUtil;
import org.junit.Test;

/**
 * @author tao.xiong
 * @Description redis 测试
 * @Date 2022/1/18 15:51
 */
public class JedisRedisConfigTest extends FrameworkDbApplicationTest {

    @Test
    public void query() {
        RedisUtil.inst().delete("strKey");
        RedisUtil.inst().opsForSet().add("strKey", "11");
        System.out.println(RedisUtil.inst().opsForValue().get("strKey"));
    }

}