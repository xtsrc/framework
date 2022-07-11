package com.xt.framework.job.demo;

import com.xt.framework.job.core.annotation.ElasticSimpleJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.elasticjob.api.ShardingContext;
import org.apache.shardingsphere.elasticjob.simple.job.SimpleJob;

/**
 * @author tao.xiong
 * @Description simple job
 * @Date 2022/1/20 10:55
 */
@Slf4j
@ElasticSimpleJob(jobName = "test01", corn = "0 0/1 * * * ?")
public class MySimpleJob implements SimpleJob {
    @Override
    public void execute(ShardingContext shardingContext) {
        String ss=String.format("Thread ID: %s, 作业分片总数: %s, " +
                        "当前分片项: %s.当前参数: %s," +
                        "作业名称: %s.作业自定义参数: %s",
                Thread.currentThread().getId(),
                shardingContext.getShardingTotalCount(),
                shardingContext.getShardingItem(),
                shardingContext.getShardingParameter(),
                shardingContext.getJobName(),
                shardingContext.getJobParameter()
        );
        log.info(ss);
    }
}
