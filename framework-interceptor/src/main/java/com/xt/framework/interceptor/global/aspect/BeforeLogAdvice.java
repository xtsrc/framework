package com.xt.framework.interceptor.global.aspect;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;

/**
 * @author tao.xiong
 * @Description SpringAPI接口
 * @Date 2022/11/1 17:45
 */
@Component
@Slf4j
public class BeforeLogAdvice implements MethodBeforeAdvice {
    @Override
    public void before(@Nonnull Method method, @Nonnull Object[] args, Object target) {
        if (target == null) {
            return;
        }
        log.info(target.getClass().getName() + "的" + method.getName() + "被执行了");
    }
}
