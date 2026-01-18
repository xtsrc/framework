package com.xt.framework.db.redis.config;

import com.xt.framework.db.redis.codec.FastJsonCodec;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

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
        log.info("【Redisson 配置】：{}", redisProperties);

        Config config = new Config();
        //编码
        config.setCodec(new FastJsonCodec());
        /*ClusterServersConfig serversConfig = config.useClusterServers()
                .addNodeAddress(redisProperties.getCluster().getNodes().toArray(new String[0]));*/
        SingleServerConfig serversConfig = config.useSingleServer().setAddress("redis://" + redisProperties.getHost() + ":" + redisProperties.getPort());
        //设置密码
        serversConfig.setPassword(redisProperties.getPassword());
        //redis连接心跳检测，防止一段时间过后，与redis的连接断开
        serversConfig.setPingConnectionInterval(32000);
        return Redisson.create(config);
    }
}
