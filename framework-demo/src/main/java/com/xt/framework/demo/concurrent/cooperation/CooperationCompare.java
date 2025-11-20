package com.xt.framework.demo.concurrent.cooperation;

import com.google.common.base.Stopwatch;
import com.xt.framwork.common.core.util.ThreadPoolTools;
import lombok.extern.slf4j.Slf4j;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * @Author: tao.xiong
 * @Date: 2020/1/15 15:08
 * @Description:
 */
@Slf4j
public final class CooperationCompare {
    private static final ExecutorService DEFAULT_EXECUTOR = ThreadPoolTools.defaultThreadPool;
    private static final Random RANDOM = new SecureRandom();


    /**
     * 计数减，达到之前不阻塞线程，计数达到之后执行
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
        log.info("countDownLatch finish elapsed:{}", stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

    /**
     * 控制访问权限，最大许可
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
        log.info("semaphore finish elapsed:{}", stopwatch.elapsed(TimeUnit.MILLISECONDS));

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
        log.info("barrier finish elapsed:{}", stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

    /**
     * 动态参与者管理、多阶段同步、父子层级结构
     */
    private static void phaserSample(){
        //模拟countDownLatch
        Stopwatch stopwatch = Stopwatch.createStarted();
        Phaser phaser=new Phaser(5);
        Phaser finalPhaser = phaser;
        IntStream.range(0, 5).forEach(i -> ThreadPoolTools.submitTask(() -> {
            try {
                log.info("phaser countDownLatch task start {}", i);
                Thread.sleep(RANDOM.nextInt(5000));
                log.info("phaser countDownLatch task finish {}", i);
            } catch (InterruptedException e) {
                log.error("exception", e);
                Thread.currentThread().interrupt();
            } finally {
                finalPhaser.arrive();
            }
        }, DEFAULT_EXECUTOR));
        phaser.awaitAdvance(phaser.getPhase());
        log.info("phaser countDownLatch finish {}", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        //模拟barrier
        stopwatch = Stopwatch.createStarted();
        phaser=new Phaser(5);
        Phaser finalPhaser1 = phaser;
        IntStream.range(0, 5).forEach(i -> ThreadPoolTools.submitTask(() -> {
            try {
                log.info("phaser barrier task start {}", i);
                finalPhaser1.arriveAndAwaitAdvance();
                Thread.sleep(RANDOM.nextInt(5000));
                log.info("phaser barrier task finish {}", i);
            } catch (InterruptedException e) {
                log.error("exception", e);
                Thread.currentThread().interrupt();
            }
        }, DEFAULT_EXECUTOR));
        phaser.awaitAdvance(phaser.getPhase());
        log.info("phaser barrier finish elapsed:{}", stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

    /**
     * 轻量级双向数据交换
     */
    private static void exchangerSample(){
        Exchanger<String> exchanger=new Exchanger<>();
        new Thread(()->{
            try{
                String dataA="Data from Thread-A";
                String received=exchanger.exchange(dataA,2,TimeUnit.SECONDS);
                log.info("Thread-A received:{}", received);
            }catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (TimeoutException e) {
                log.info("Thread-A exchange timeout");
            }
        }).start();
        new Thread(()->{
            try{
                String dataB="Data from Thread-B";
                String received=exchanger.exchange(dataB);
                log.info("Thread-B received:{}", received);
            }catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        //缓冲区交换（生产者-消费者）
        Exchanger<List<String>> bufferExchanger=new Exchanger<>();
        //生产者线程
        new Thread(()->{
            List<String> buffer=new ArrayList<>();
            while (true){
                try {
                    //生产数据
                    buffer.add("1");
                    log.info("生产线程生产了数据：{}",buffer.get(0));
                    //交换缓冲区
                    buffer=bufferExchanger.exchange(buffer);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
        //消费者线程
        new Thread(()->{
            List<String> buffer= Collections.emptyList();
            while (true){
                try {
                    //交换缓冲区
                    buffer=bufferExchanger.exchange(buffer);
                    //消费数据
                    log.info("消费线程消费了数据：{}",buffer.get(0));
                    buffer.clear();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    public static void main(String[] args) {
        try {
            countDownLatchSample();
            semaphoreSample();
            barrierSample();
            phaserSample();
            exchangerSample();
            DEFAULT_EXECUTOR.shutdown();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
