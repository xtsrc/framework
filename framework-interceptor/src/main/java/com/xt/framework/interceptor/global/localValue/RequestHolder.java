package com.xt.framework.interceptor.global.localValue;

import com.xt.framework.db.api.dto.LogInfo;
import com.xt.framwork.common.core.constant.Constants;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.UUID;

/**
 * @author tao.xiong
 * @Description 请求过程变量容器
 * @Date 2022/7/26 15:13
 */
public class RequestHolder extends RequestContextHolder {
    private RequestHolder() {
        throw new IllegalStateException("Utility class");
    }

    private static final ThreadLocal<LogInfo> LOG_INFO_THREAD_LOCAL = new ThreadLocal<>();

    public static void release() {
        LOG_INFO_THREAD_LOCAL.remove();
        MDC.remove(Constants.TRACE_ID);
    }

    public static LogInfo getLogInfoThreadLocal() {
        return LOG_INFO_THREAD_LOCAL.get();
    }

    public static void setLogInfoThreadLocal(HttpServletRequest request) {
        LogInfo logInfo = new LogInfo();
        logInfo.setStartTime((Long) request.getAttribute(Constants.BEGIN_TIME));
        logInfo.setTraceId((String) request.getAttribute(Constants.TRACE_ID));
        logInfo.setUri(request.getRequestURI());
        String queryString = request.getQueryString();
        logInfo.setQueryString(null == queryString ? "" : queryString);
        logInfo.setMethod(request.getMethod());
        logInfo.setIp(getIpAddress());
        logInfo.setToken(getToken());
        LOG_INFO_THREAD_LOCAL.set(logInfo);
    }

    public static void init() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(getRequestAttributes())).getRequest();
        //如果有上层调用就用上层的ID
        String traceId = request.getHeader(Constants.TRACE_ID);
        if (traceId == null) {
            traceId = UUID.randomUUID().toString();
        }
        MDC.put(Constants.TRACE_ID, traceId);
        request.setAttribute(Constants.BEGIN_TIME, System.currentTimeMillis());
        request.setAttribute(Constants.REQUEST_ID, traceId);
        request.setAttribute(Constants.TRACE_ID, traceId);
        setLogInfoThreadLocal(request);
    }


    /**
     * 获取ip地址
     *
     * @return ip
     */
    public static String getIpAddress() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(getRequestAttributes())).getRequest();
        String ip = null;
        String unknown = "unknown";
        String ipAddresses = request.getHeader("X-Forwarded-For");
        if (ipAddresses == null || ipAddresses.length() == 0 || unknown.equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || unknown.equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || unknown.equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || unknown.equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getHeader("X-Real-IP");
        }
        if (ipAddresses != null && ipAddresses.length() != 0) {
            ip = ipAddresses.split(",")[0];
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ipAddresses)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 获取当前token
     *
     * @return token
     */
    public static String getToken() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(getRequestAttributes())).getRequest();
        String token = request.getHeader(Constants.TOKEN);
        if (StringUtils.isEmpty(token)) {
            token = request.getParameter(Constants.TOKEN);
        }
        return token;
    }

}
