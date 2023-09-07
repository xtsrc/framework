package com.xt.framework.demo.async;

import com.google.common.util.concurrent.*;
import com.xt.framwork.common.core.util.ThreadPoolTools;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author tao.xiong
 * @Description 异步
 * @Date 2021/8/27 11:01
 */
@Slf4j
public class AsyncCompare {
    private static final ExecutorService executorService = ThreadPoolTools.defaultThreadPool;
    private static final Runnable RUNNABLE = () -> System.out.println("RUNNABLE执行无返回结果的异步任务 PROCESSING!");
    private static String operator="操作对象";
    private static final Consumer<String> CONSUMER = (s) -> {
        operator += s+"CONSUMER DONE!";
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("CONSUMER处理返回值:" + operator + "输入，不输出 PROCESSING!");
    };
    private static final Supplier<String> SUPPLIER = () -> {
        System.out.println("SUPPLIER执行有返回值的异步任务 PROCESSING!");
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "SUPPLIER DONE!";
    };
    private static final Callable<String> CALLABLE = () -> {
        System.out.println("CALLABLE执行有返回值的异步任务 PROCESSING!");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "CALLABLE DONE!";
    };
    private static final Function<String, String> FUNCTION = (s) -> {
        System.out.println("FUNCTION处理返回值:" + s + "输入,输出 PROCESSING!");
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return s + "FUNCTION DONE!";
    };

    public static void asyncByGuava() {
        ListeningExecutorService executorService = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());
        final ListenableFuture<String> listenableFuture = executorService.submit(CALLABLE);
        Futures.addCallback(listenableFuture, new FutureCallback<String>() {
            @Override
            public void onSuccess(@Nullable String s) {
                CONSUMER.accept(s);
                System.out.println("asyncByGuava 异步回调 CONSUMER 处理 CALLABLE 结果:"+operator);
            }

            @Override
            public void onFailure(@NotNull Throwable throwable) {
                log.error("异步任务执行失败：{}", throwable.getMessage());
            }
        }, executorService);
    }

    @SneakyThrows
    public static void asyncByJava() {
        ThreadPoolTools.submitTask(RUNNABLE, executorService);
        Future<String> future = ThreadPoolTools.submitTask(CALLABLE, executorService);
        if (future.isDone()) {
            System.out.println("future get阻塞 异步返回：" + future.get());
        }
        CompletableFuture<Void> completableFuture = ThreadPoolTools.submitAsyncTask(RUNNABLE, executorService);
        completableFuture.whenComplete((t, action) -> System.out.println("completableFuture 异步回调，CONSUMER processing"));
        String result = completableFuture.handle((t, action) -> {
            System.out.println("completableFuture 异步回调，BiFunction processing");
            return "BiFunction Done!";
        }).get();
        System.out.println("future 异步回调，BiFunction处理结果：" + result);
        completableFuture.exceptionally(t -> {
            System.out.println("completableFuture 回调异步无返回结果执行失败：" + t.getMessage());
            return null;
        }).join();
        CompletableFuture<String> completableFutureReturn = ThreadPoolTools.submitAsyncTask(SUPPLIER, executorService);
        completableFutureReturn.thenAccept(CONSUMER);
        Thread.sleep(100);
        System.out.println("completableFutureReturn 异步回调  CONSUMER处理SUPPLIER 结果: " + operator);
        String functionResult=completableFutureReturn.thenApply(FUNCTION).get();
        System.out.println("completableFutureReturn 异步回调  FUNCTION处理SUPPLIER 结果: " + functionResult);
    }

    public static void main(String[] args) {
        asyncByGuava();
        asyncByJava();
    }
}
