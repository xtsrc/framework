package com.xt.framework.interceptor.global.filter;

import com.xt.framwork.common.core.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Nonnull;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author tao.xiong
 * @Description 只拦截一次
 * servlet-2.3中，Filter会过滤一切请求 包括 内部forward和<%@ include file="/index.jsp"%>
 * servlet-2.4中Filter默认下只拦截外部提交的请求
 * @Date 2022/11/1 17:00
 */
@Component
@Slf4j
public class MyOnceFilter extends OncePerRequestFilter {
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        //排除内部api调用
        return new AntPathMatcher().match("/**/api/**", request.getServletPath());
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull FilterChain filterChain) throws ServletException, IOException {
        log.info("OncePerRequestFilter start");
        String token = request.getHeader(Constants.AUTHORIZATION);
        if (!StringUtils.isEmpty(token)) {
            filterChain.doFilter(request, response);
        }
        log.info("OncePerRequestFilter finish");
    }
}
