package com.xt.framework.interceptor.global.aspect;

import com.alibaba.fastjson.JSON;
import com.xt.framwork.common.core.bean.Response;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author tao.xiong
 * @Description 主要是进行公共方法的可以拿得到方法响应中参数的值，但是拿不到原始的Http请求和相对应响应的方法,属于方法级别的拦截器。
 * 注解方式
 * @Date 2022/5/13 10:41
 */
@Aspect
@Component
@Slf4j
public class AopWebLogAspect {
    /**
     * 以Controller包下定义所有请求的方法
     */
    @Pointcut("execution(public * com.xt.framework..*Controller.*(..)) ")
    private void webLog() {
        // 切点
    }

    /**
     * 在切入点之前进行
     */
    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        log.info("========================================== Start ==========================================");
        //打印请求参数相关日志
        // 打印请求 url
        log.info("url:{}", request.getRequestURI());
        // 打印 Http method
        log.info("HTTP Method:{}", request.getMethod());
        // 打印调用 controller 的全路径以及执行方法
        log.info("Class Method:{}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        // 打印请求的 IP
        log.info("IP :{}", request.getRemoteAddr());
        // 打印请求入参
        log.info("Request Args:{}", JSON.toJSONString(joinPoint.getArgs()));
    }

    /**
     * 在切入点之后执行
     */
    @After("webLog()")
    public void doAfter() {
        log.info("========================================== end ==========================================");
    }

    @Around("webLog()")
    public Object doAroud(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Response result = (Response) proceedingJoinPoint.proceed();
        if (result != null) {
            //打印出参
            log.info("Response Args : {}", JSON.toJSONString(result));
            // 执行耗时
            log.info("Time-Consuming : {} ms", System.currentTimeMillis() - startTime);
        }
        return result;
    }
}
