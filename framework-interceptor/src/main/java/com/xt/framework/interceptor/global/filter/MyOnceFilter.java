package com.xt.framework.interceptor.global.filter;

import cn.hutool.core.util.ObjectUtil;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Nonnull;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tao.xiong
 * @Description 只拦截一次
 * servlet-2.3中，Filter会过滤一切请求 包括 内部forward和<%@ include file="/index.jsp"%>
 * servlet-2.4中Filter默认下只拦截外部提交的请求
 * @Date 2022/11/1 17:00
 */
@Component
public class MyOnceFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("token");
        if (!StringUtils.isEmpty(token)) {
            filterChain.doFilter(request, response);
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Map<String, Object> map = new HashMap<>(16);
        map.put("name", "李四");
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(ObjectUtil.serialize(map));
        outputStream.close();
    }
}
