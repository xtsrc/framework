package com.xt.framework.db.mysql.service;

import com.xt.framework.db.cache.Constants;
import com.xt.framework.db.mysql.service.dto.SyAdminInfo;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author tao.xiong
 * @Description 测试
 * @Date 2022/11/30 15:08
 */
@SpringBootTest
class SyAdminCacheServiceTest {
    @Resource
    private SyAdminCacheService syAdminCacheService;
    @Resource
    private RedissonClient redissonClient;

    @Test
    void willCacheData() {
        SyAdminInfo hotData = syAdminCacheService.getValueFilter(1L);
        System.out.println(hotData);
    }
    @Test
    void test(){
        String cacheKey = String.format(Constants.CACHE_PREFIX, String.format(Constants.CACHE_KEY, 1L));
        // 获取缓存
        RBucket<SyAdminInfo> rBucket = redissonClient.getBucket(cacheKey);
        SyAdminInfo syAdminInfo=rBucket.get();
        System.out.println(syAdminInfo);
    }
}