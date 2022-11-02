package com.xt.framework.interceptor.global.interceptor;

import com.xt.framework.db.api.ILogServiceApi;
import com.xt.framework.db.api.dto.LogInfo;
import com.xt.framework.interceptor.global.localValue.RequestHolder;
import com.xt.framwork.common.core.constant.Constants;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Nonnull;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

    /**
     * 控制器方法调用之前会进行
     *
     * @param request  请求
     * @param response 返回
     * @param handler  处理器
     * @return true就是选择可以调用后面的方法
     */
    @Override
    public boolean preHandle(HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler) {
        RequestHolder.init();
        log.info("请求开始：url:{},args:{}", request.getRequestURI(), request.getQueryString());
        if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            ApiOperation apiOperation = handlerMethod.getMethodAnnotation(ApiOperation.class);
            if (null != apiOperation) {
                RequestHolder.getLogInfoThreadLocal().setDescription(apiOperation.value());
            }
        }
        return true;
    }

    /**
     * 控制后方法执行之后会进行，抛出异常则不会被执行
     *
     * @param request      请求
     * @param response     返回
     * @param handler      处理
     * @param modelAndView mv
     */
    @Override
    public void postHandle(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler, ModelAndView modelAndView) {
        //
    }

    /**
     * 方法被调用或者抛出异常都会被执行
     *
     * @param request  请求
     * @param response 返回
     * @param handler  处理
     * @param ex       异常
     */
    @Override
    public void afterCompletion(HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler, Exception ex) {
        long beginTime = (Long) request.getAttribute(Constants.BEGIN_TIME);
        log.info("请求结束：url:{},耗时:{}ms", request.getRequestURL(), System.currentTimeMillis() - beginTime);
        LogInfo logInfo = RequestHolder.getLogInfoThreadLocal();
        if (null != logInfo) {
            logInfo.setEndTime(System.currentTimeMillis());
            logServiceApi.save(logInfo);
        }

        RequestHolder.release();
    }
}
