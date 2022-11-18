package com.xt.framework.interceptor.global.aspect;

import com.xt.framework.interceptor.global.annotation.HintShardingWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.hint.HintManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author tao.xiong
 * @Description 强制分片aspect
 * @Date 2022/11/17 17:02
 */
@Aspect
@Component
@Slf4j
public class HitShardingAspect {
    HintManager hintManager;

    @Pointcut("@annotation(com.xt.framework.interceptor.global.annotation.HintShardingWrapper)")
    public void hitShardingAspect() {
        //切点定义
    }

    @Before("hitShardingAspect()")
    public void before() {
        HintManager.clear();
    }

    @Around("hitShardingAspect()")
    public Object sharding(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;
        String method = joinPoint.getSignature().getName();
        HintShardingWrapper aopMark = getAopMark(joinPoint, method);
        if (aopMark != null) {
            hintManager = HintManager.getInstance();
            hintManager.addTableShardingValue(aopMark.logicTable(), aopMark.tableShardingValue());
            hintManager.addDatabaseShardingValue(aopMark.logicTable(), aopMark.databaseShardingValue());
            hintManager.setMasterRouteOnly();
        }
        result = joinPoint.proceed();
        return result;
    }

    @After("hitShardingAspect()")
    public void after() {
        hintManager.close();
    }

    private HintShardingWrapper getAopMark(ProceedingJoinPoint point, String methodName) {
        try {
            Class<?> targetClass = point.getTarget().getClass();
            Class<?>[] parameterTypes = ((MethodSignature) point.getSignature()).getParameterTypes();
            Method method = targetClass.getMethod(methodName, parameterTypes);
            return method.getAnnotation(HintShardingWrapper.class);
        } catch (Exception e) {
            log.error("{} getAopMark error", methodName, e);
            return null;
        }
    }
}
