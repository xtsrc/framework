package com.xt.framwork.common.core.util;

import com.github.rholder.retry.*;
import lombok.SneakyThrows;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author tao.xiong
 * @Description 服务工具包
 * @Date 2022/2/25 15:44
 */
public class ServiceUtil {
    @SneakyThrows
    public static <T> T retry(Supplier<T> supplier) {
        Retryer<T> retryer = RetryerBuilder.<T>newBuilder()
                //retryIf 重试条件
                .retryIfException()
                .retryIfRuntimeException()
                .retryIfExceptionOfType(Exception.class)
                .retryIfException(throwable -> Objects.equals(throwable, new Exception()))
                .retryIfResult(Objects::isNull)
                //等待策略：每次请求间隔1s
                .withWaitStrategy(WaitStrategies.fixedWait(1, TimeUnit.SECONDS))
                //停止策略 : 尝试请求3次
                .withStopStrategy(StopStrategies.stopAfterAttempt(3))
                //时间限制
                .withAttemptTimeLimiter(AttemptTimeLimiters.fixedTimeLimit(2, TimeUnit.SECONDS))
                .build();
        Callable<T> callable = supplier::get;
        return retryer.call(callable);
    }
}
