package com.xt.framework.db.redis.config;

import com.xt.framework.db.FrameworkDbApplicationTest;
import com.xt.framework.db.redis.util.RedisUtil;
import com.xt.framwork.common.core.util.UuidUtil;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author tao.xiong
 * @Description redis 测试
 * @Date 2022/1/18 15:51
 */
public class JedisConfigTest extends FrameworkDbApplicationTest {

    @Test
    public void query() {
        RedisUtil.inst().delete("strKey");
        RedisUtil.inst().opsForSet().add("strKey", "11");
        System.out.println(RedisUtil.inst().opsForValue().get("strKey"));
    }

    @Test
    public void testRedis() throws InterruptedException {
        int totalThreads = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(totalThreads);

        CountDownLatch countDownLatch = new CountDownLatch(totalThreads);
        for (int i = 0; i < totalThreads; i++) {
            String threadId = String.valueOf(i);
            executorService.execute(() -> {
                try {
                    testLock(UuidUtil.getUUID(), threadId);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        // After all thread done, acquire again, expect to be successful.
        testLock("hey", "final success");
    }

    public void testLock(String key, String threadId) throws InterruptedException {
        String value = UuidUtil.getUUID();
        boolean lock = RedisUtil.tryLock(key, value);
        if (lock) {
            System.out.println("Successfully got lock - " + threadId);
            Thread.sleep(2000);
            RedisUtil.unlock(key, value);
        } else {
            System.out.println("Failed to obtain lock - " + threadId);
        }
    }

}