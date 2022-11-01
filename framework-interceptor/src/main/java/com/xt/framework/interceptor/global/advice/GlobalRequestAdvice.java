package com.xt.framework.interceptor.global.advice;

import com.xt.framework.db.api.dto.LogInfo;
import com.xt.framework.interceptor.global.localValue.RequestHolder;
import org.apache.commons.io.IOUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import javax.annotation.Nonnull;
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
    public boolean supports(@Nonnull MethodParameter methodParameter, @Nonnull Type targetType,
                            @Nonnull Class<? extends HttpMessageConverter<?>> converterType) {
        // 这里返回true才会执行beforeBodyRead
        return true;
    }

    @Override
    public Object handleEmptyBody(Object body, @Nonnull HttpInputMessage inputMessage, @Nonnull MethodParameter parameter,
                                  @Nonnull Type targetType, @Nonnull Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Override
    public @Nonnull
    HttpInputMessage beforeBodyRead(@Nonnull HttpInputMessage inputMessage, @Nonnull MethodParameter parameter, @Nonnull Type targetType,
                                    @Nonnull Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        LogInfo logInfo = RequestHolder.getLogInfoThreadLocal();
        if (null != logInfo) {
            String body = IOUtils.toString(inputMessage.getBody(), StandardCharsets.UTF_8);
            // 设置请求正文，这里拿到的是InputStream的内容
            logInfo.setBody(body);
            // InputStream只能读一次，这里读了，得重新返回一个新的
            return new HttpInputMessage() {
                @Override
                public @Nonnull
                HttpHeaders getHeaders() {
                    return inputMessage.getHeaders();
                }

                @Override
                public @Nonnull
                InputStream getBody() {
                    return new ByteArrayInputStream(body.getBytes());
                }
            };
        }
        return inputMessage;
    }

    @Override
    public @Nonnull
    Object afterBodyRead(@Nonnull Object body, @Nonnull HttpInputMessage inputMessage, @Nonnull MethodParameter parameter, @Nonnull Type targetType,
                         @Nonnull Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }
}
