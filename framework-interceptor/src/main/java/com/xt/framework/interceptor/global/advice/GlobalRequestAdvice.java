package com.xt.framework.interceptor.global.advice;

import com.xt.framework.db.api.service.dto.LogInfo;
import com.xt.framework.interceptor.global.localValue.RequestHolder;
import org.apache.commons.io.IOUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * @author tao.xiong
 * @Description advice 请求
 * @Date 2022/7/26 14:53
 */
public class GlobalRequestAdvice implements RequestBodyAdvice {

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        // 这里返回true才会执行beforeBodyRead
        return true;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter,
                                  Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                           Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        LogInfo logInfo = RequestHolder.getLogInfoThreadLocal();
        if (null != logInfo) {
            String body = IOUtils.toString(inputMessage.getBody(), StandardCharsets.UTF_8);
            // 设置请求正文，这里拿到的是InputStream的内容
            logInfo.setBody(body);
            // InputStream只能读一次，这里读了，得重新返回一个新的
            return new HttpInputMessage() {
                @Override
                public HttpHeaders getHeaders() {
                    return inputMessage.getHeaders();
                }

                @Override
                public InputStream getBody() {
                    return new ByteArrayInputStream(body.getBytes());
                }
            };
        }
        return inputMessage;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }
}
