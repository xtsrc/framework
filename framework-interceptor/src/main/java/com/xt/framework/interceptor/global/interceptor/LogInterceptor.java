package com.xt.framework.interceptor.global.interceptor;

import com.xt.framework.db.api.service.ILogServiceApi;
import com.xt.framework.db.api.service.dto.LogInfo;
import com.xt.framework.interceptor.global.localValue.RequestHolder;
import com.xt.framwork.common.core.constant.Constants;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author tao.xiong
 * @Description 日志拦截器
 * @Date 2022/2/25 11:30
 */
@Slf4j
@Component
public class LogInterceptor implements HandlerInterceptor {
    @Resource
    private ILogServiceApi logServiceApi;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //如果有上层调用就用上层的ID
        String traceId = request.getHeader(Constants.TRACE_ID);
        if (traceId == null) {
            traceId = UUID.randomUUID().toString();
        }
        MDC.put(Constants.TRACE_ID, traceId);
        request.setAttribute(Constants.BEGIN_TIME, System.currentTimeMillis());
        request.setAttribute(Constants.REQUEST_ID, traceId);
        request.setAttribute(Constants.TRACE_ID, traceId);
        log.info("请求开始：url:{},args:{}", request.getRequestURI(), request.getQueryString());

        if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
            // 在这里新增logger的记录
            RequestHolder.setLogInfoThreadLocal(request);
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            ApiOperation apiOperation = handlerMethod.getMethodAnnotation(ApiOperation.class);
            if (null != apiOperation) {
                RequestHolder.getLogInfoThreadLocal().setDescription(apiOperation.value());
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        //调用结束后删除
        MDC.remove(Constants.TRACE_ID);
        long beginTime = (Long) request.getAttribute(Constants.BEGIN_TIME);
        log.info("请求结束：url:{},耗时:{}ms", request.getRequestURI(), System.currentTimeMillis() - beginTime);
        LogInfo logInfo = RequestHolder.getLogInfoThreadLocal();
        if (null != logInfo) {
            logInfo.setEndTime(System.currentTimeMillis());
            logServiceApi.save(logInfo);
        }
        RequestHolder.release();
    }
}
