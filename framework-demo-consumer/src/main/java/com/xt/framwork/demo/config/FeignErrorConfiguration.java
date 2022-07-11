package com.xt.framwork.demo.config;

import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author tao.xiong
 * @Description 错误处理
 * @Date 2022/7/8 17:55
 */
@Slf4j
public class FeignErrorConfiguration {
    @Bean
    public ErrorDecoder errorDecoder() {
        return new UserErrorDecoder();
    }

    /**
     * 自定义错误
     */
    public static class UserErrorDecoder implements ErrorDecoder {
        @Override
        public Exception decode(String methodKey, Response response) {
            Exception exception = null;
            try {
                // 获取原始的返回内容
                String json = Util.toString(response.body().asReader(Charset.defaultCharset()));
                exception = new RuntimeException(json);
                // 将返回内容反序列化为Result，这里应根据自身项目作修改

            } catch (IOException ex) {
                log.error(ex.getMessage(), ex);
            }
            return exception;
        }
    }
}
