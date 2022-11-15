package com.xt.framework.demo.current;

import com.google.common.base.Stopwatch;
import com.xt.framwork.common.core.util.ThreadPoolTools;
import lombok.extern.slf4j.Slf4j;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * @Author: tao.xiong
 * @Date: 2020/1/15 15:08
 * @Description:
 */
@Slf4j
public final class ConcurrentCompare {
    private static final ExecutorService DEFAULT_EXECUTOR = ThreadPoolTools.defaultThreadPool;
    private static final Random RANDOM = new SecureRandom();


    /**
     * 计数减，达到之前不阻塞线程；
     */
    private static void countDownLatchSample() throws InterruptedException {
        Stopwatch stopwatch = Stopwatch.createStarted();
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        IntStream.range(0, 5).forEach(i -> ThreadPoolTools.submitTask(() -> {
            try {
                log.info("countDownLatch task start {}", i);
                Thread.sleep(RANDOM.nextInt(5000));
                log.info("countDownLatch task finish {}", i);
            } catch (InterruptedException e) {
                log.error("exception", e);
                Thread.currentThread().interrupt();
            } finally {
                countDownLatch.countDown();
            }
        }, DEFAULT_EXECUTOR));
        countDownLatch.await();
        log.info("countDownLatch finish {}", stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

    /**
     * 控制访问权限
     */
    private static void semaphoreSample() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        final Semaphore semaphore = new Semaphore(3);
        final int timeOut = 5000;
        IntStream.range(0, 5).forEach(i -> ThreadPoolTools.submitTask(() -> {
            try {
                if (semaphore.tryAcquire(timeOut, TimeUnit.MILLISECONDS)) {
                    log.info("semaphore task start {}", i);
                    Thread.sleep(RANDOM.nextInt(5000));
                    log.info("semaphore task finish {}", i);
                    semaphore.release();
                }
            } catch (InterruptedException e) {
                log.error("exception", e);
                Thread.currentThread().interrupt();
            }
        }, DEFAULT_EXECUTOR));
        log.info("semaphore finish {}", stopwatch.elapsed(TimeUnit.MILLISECONDS));

    }

    /**
     * 计数加，达到之前阻塞执行；可重复利用
     */
    private static void barrierSample() throws NoSuchAlgorithmException {
        Stopwatch stopwatch = Stopwatch.createStarted();
        CyclicBarrier barrier = new CyclicBarrier(2);

        IntStream.range(0, 5).forEach(i -> ThreadPoolTools.submitTask(() -> {
            try {
                log.info("barrier task start {}", i);
                barrier.await();
                Thread.sleep(RANDOM.nextInt(5000));
                log.info("barrier task finish {}", i);
            } catch (InterruptedException | BrokenBarrierException e) {
                log.error("exception", e);
                Thread.currentThread().interrupt();
            }
        }, DEFAULT_EXECUTOR));
        log.info("barrier finish {}", stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

    public static void main(String[] args) {
        try {
            countDownLatchSample();
            semaphoreSample();
            barrierSample();
            DEFAULT_EXECUTOR.shutdown();
        } catch (InterruptedException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }
}
