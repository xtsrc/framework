package com.xt.framework.db.redis.config;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author tao.xiong
 * @Description redisson 客户端
 * @Date 2022/7/5 17:23
 */
@Slf4j
@Configuration
public class RedissonConfig {
    @Resource
    private RedisProperties redisProperties;

    /**
     * 配置redisson集群
     *
     * @return redisson
     */
    @Bean(destroyMethod = "shutdown")
    public RedissonClient getRedissonClient() {
        List<String> clusterNodes = redisProperties.getCluster().getNodes();
        log.info("【Redisson 配置】：{}", redisProperties);

        Config config = new Config();
        //对象编码选择纯字符串编码
        config.setCodec(StringCodec.INSTANCE);
        ClusterServersConfig clusterServersConfig = config.useClusterServers()
                .addNodeAddress(clusterNodes.toArray(new String[0]));
        //设置密码
        clusterServersConfig.setPassword(redisProperties.getPassword());
        //redis连接心跳检测，防止一段时间过后，与redis的连接断开
        clusterServersConfig.setPingConnectionInterval(32000);
        return Redisson.create(config);
    }
}
