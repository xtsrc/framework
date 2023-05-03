package com.xt.framework.interceptor.global.aspect;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.Subscribe;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author luojiaheng
 * @date 2019/6/25 10:36
 */
@Component
public class GuavaEventBusProcessor implements BeanPostProcessor {

    /**
     * 事件总线bean由Spring IoC容器负责创建，这里只需要通过@Autowired注解注入该bean即可使用事件总线
     */
    @Autowired
    AsyncEventBus asyncEventBus;

    @Override
    public Object postProcessBeforeInitialization(@NotNull Object bean, @NotNull String beanName) throws BeansException {
        return bean;
    }

    /**
     * 对于每个容器执行了初始化的 bean，如果这个 bean 的某个方法注解了@Subscribe,则将该 bean 注册到事件总线
     */
    @Override
    public Object postProcessAfterInitialization(Object bean,@NotNull String beanName) throws BeansException {

        Method[] methods = bean.getClass().getMethods();
        for (Method method : methods) {
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation.annotationType().equals(Subscribe.class)) {
                    // 如果这是一个Guava @Subscribe注解的事件监听器方法，说明所在bean实例
                    // 对应一个Guava事件监听器类，将该bean实例注册到Guava事件总线
                    asyncEventBus.register(bean);
                    return bean;
                }
            }
        }

        return bean;
    }

}
