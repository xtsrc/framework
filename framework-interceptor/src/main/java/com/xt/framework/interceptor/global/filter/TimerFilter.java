package com.xt.framework.interceptor.global.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * @author tao.xiong
 * @Description 可以获得Http原始的请求和响应信息，但是拿不到响应方法的信息
 * 通过注解实现
 * @Date 2022/11/1 16:33
 */
@Slf4j
@Component
@WebFilter(filterName = "TimerFilter", urlPatterns = "/*")
@Order(7)
public class TimerFilter implements Filter {
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
        log.info("time filter start ");
        long start = System.currentTimeMillis();
        chain.doFilter(request, response);
        log.info("time filter finish:" + (System.currentTimeMillis() - start));
    }

    @Override
    public void destroy() {
        log.info("" + getClass() + " destroy");
    }
}
