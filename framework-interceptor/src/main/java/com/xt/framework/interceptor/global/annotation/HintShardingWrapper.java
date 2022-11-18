package com.xt.framework.interceptor.global.annotation;

import java.lang.annotation.*;

/**
 * @author tao.xiong
 * @Description 强制分片策略
 * @Date 2022/11/17 16:54
 */
@Target({ElementType.PARAMETER, ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HintShardingWrapper {
    String logicTable();

    String tableShardingValue();

    String databaseShardingValue();
}
