package com.xt.framework.job.core.annotation;

import org.apache.shardingsphere.elasticjob.api.JobConfiguration;
import org.apache.shardingsphere.elasticjob.lite.internal.schedule.JobScheduler;
import org.apache.shardingsphere.elasticjob.reg.base.CoordinatorRegistryCenter;
import org.apache.shardingsphere.elasticjob.simple.job.SimpleJob;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;

/**
 * @author tao.xiong
 * @Description 切面
 * @Date 2022/1/20 14:12
 */
@Configuration
@ConditionalOnBean(CoordinatorRegistryCenter.class)
@ConditionalOnExpression("'${reg-center.server-lists}'.length() > 0")
public class ElasticSimpleJobAspect {
    @Resource
    private CoordinatorRegistryCenter zkCenter;

    @PostConstruct
    public void initSimpleJob() {
        Map<String, Object> beans = SpringBeanUtil.getBeansWithAnnotation(ElasticSimpleJob.class);
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            Object instance = entry.getValue();
            Class<?>[] interfaces = instance.getClass().getInterfaces();
            for (Class<?> anInterface : interfaces) {
                if (anInterface == SimpleJob.class) {
                    ElasticSimpleJob annotation = instance.getClass().getAnnotation(ElasticSimpleJob.class);
                    String jobName = annotation.jobName();
                    String corn = annotation.corn();
                    int shardingTotalCount = annotation.shardingTotalCount();
                    boolean overwrite = annotation.overwrite();
                    JobConfiguration jobConfiguration = JobConfiguration.newBuilder(jobName, shardingTotalCount)
                            .cron(corn).overwrite(overwrite).build();
                    new JobScheduler(zkCenter, (SimpleJob) instance, jobConfiguration);
                }
            }
        }
    }

}
