package com.xt.framework.interceptor.global.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author tao.xiong
 * @Description 通过config实现
 * @Date 2022/11/1 16:38
 */
@Slf4j
public class TimerConfigFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {
        log.info("" + getClass() + " init");
    }

    /**
     * 在这方法中进行拦截
     *
     * @param request  请求
     * @param response 返回
     * @param chain    过滤链
     * @throws IOException      io异常
     * @throws ServletException servlet异常
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("time filter start class is {}", getClass());
        long start = System.currentTimeMillis();
        chain.doFilter(request, response);
        log.info("time filter:" + (System.currentTimeMillis() - start));
        log.info("time filter finish");
    }

    @Override
    public void destroy() {
        log.info("" + getClass() + " init");
    }
}
