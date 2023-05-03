package com.xt.framework.demo.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

/**
 * @author tao.xiong
 * @date 2023/4/11 16:11
 * @desc 异步注解
 */
@Component
@Slf4j
public class AsyncAnnotation {
    /**
     * 带参数的异步调用 异步方法可以传入参数
     * 对于返回值是void，异常会被AsyncUncaughtExceptionHandler处理掉
     *
     * @param s 入参
     */
    @Async
    public void asyncInvokeWithException(String s) {
        log.info("asyncInvokeWithParameter, parementer={}", s);
        throw new IllegalArgumentException(s);
    }

    /**
     * 异常调用返回Future
     * 对于返回值是Future，不会被AsyncUncaughtExceptionHandler处理，需要我们在方法中捕获异常并处理
     * 或者在调用方在调用Futrue.get时捕获异常进行处理
     *
     * @param i 入参
     * @return Future
     */
    @Async
    public Future<String> asyncInvokeReturnFuture(int i) {
        log.info("asyncInvokeReturnFuture, parementer={}", i);
        Future<String> future;
        try {
            Thread.sleep(1000 * 1);
            future = new AsyncResult<String>("success:" + i);
            throw new IllegalArgumentException("a");
        } catch (InterruptedException e) {
            future = new AsyncResult<String>("error");
        } catch (IllegalArgumentException e) {
            future = new AsyncResult<String>("error-IllegalArgumentException");
        }
        return future;
    }
}
