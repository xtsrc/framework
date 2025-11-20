package com.xt.framework.demo.async;

import com.xt.framework.demo.FrameworkDemoApplicationTest;
import com.xt.framework.demo.concurrent.division.async.AsyncAnnotation;
import org.junit.Test;

import javax.annotation.Resource;

import java.util.concurrent.Future;

/**
 * @author tao.xiong
 * @date 2023/4/11 16:16
 * @desc 异步测试
 */public class AsyncAnnotationTest extends FrameworkDemoApplicationTest {
     @Resource
     private AsyncAnnotation asyncAnnotation;

    @Test
    public void asyncInvokeWithException() {
        asyncAnnotation.asyncInvokeWithException("222");
    }

    @Test
    public void asyncInvokeReturnFuture() {
        Future<String> future= asyncAnnotation.asyncInvokeReturnFuture(1);
        System.out.println(future.isDone());
    }
}