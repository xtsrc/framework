package com.xt.framework.job.core.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author tao.xiong
 * @Description 任务注解
 * @Date 2022/1/20 14:08
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface ElasticSimpleJob {
    String jobName() default "";

    String corn() default "";

    int shardingTotalCount() default 1;

    boolean overwrite() default true;
}
