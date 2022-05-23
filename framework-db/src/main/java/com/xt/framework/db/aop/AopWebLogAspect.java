package com.xt.framework.db.aop;

import com.google.gson.Gson;
import com.xt.framwork.common.core.bean.Response;
import com.xt.framwork.common.core.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.MDC;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author tao.xiong
 * @Description web请求加日志
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
    }

    /**
     * 在切入点之前进行
     */
    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
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
        log.info("Request Args:{}", new Gson().toJson(joinPoint.getArgs()));
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
            // 将线程id赋值给返回的traceId
            result.setTraceId(MDC.get(Constants.TRACE_ID));
            //打印出参
            log.info("Response Args : {}", new Gson().toJson(result));
            // 执行耗时
            log.info("Time-Consuming : {} ms", System.currentTimeMillis() - startTime);
        }
        return result;
    }
}
