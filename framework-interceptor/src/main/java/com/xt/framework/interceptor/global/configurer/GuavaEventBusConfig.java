package com.xt.framework.interceptor.global.configurer;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.xt.framework.interceptor.global.aspect.GuavaEventBusProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 该例子使用Spring注解@Configuration+@Bean的方式定义了一个事件总线bean，该bean最终的创建虽然使用了Java
 * 关键字new，但整个bean的生命周期管理都托管给了Spring IoC容器，这是很“Spring”的应用方式，避免了开发人员自己
 * 负责事件总线对象的生命周期管理
 *
 * @author tao.xiong
 * @date 2023/4/12 10:26
 */
@Configuration
public class GuavaEventBusConfig {
    /**
     * 定义事件总线bean 异步
     */
    @Bean
    public AsyncEventBus asyncEventBus() {
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(8, 16,
                60L, TimeUnit.MILLISECONDS, workQueue, new ThreadFactoryBuilder().setNameFormat("thread-%d").build());
        return new AsyncEventBus(executor);
    }
    @Bean
    @ConditionalOnMissingBean
    public GuavaEventBusProcessor guavaEventBusProcessor(){
        return new GuavaEventBusProcessor();
    }

}
