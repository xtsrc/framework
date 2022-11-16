package com.xt.framework.demo.current;

import lombok.SneakyThrows;

/**
 * @author tao.xiong
 * @Description 线程
 * @Date 2022/11/16 14:12
 */
public class ThreadDemo {
    static class Runner implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName() + "====" + i);
            }
        }
    }

    static class DelayRunner implements Runnable {
        @SneakyThrows
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName() + "====" + i);
                //运行状态->阻塞状态->可运行状态
                Thread.sleep(1000);
            }
        }
    }

    static class Threader extends Thread {
        @Override
        public void run() {
            String threadName = Thread.currentThread().getName();
            try {
                //如果线程有中断标记返回true，否则返回false
                if (Thread.currentThread().isInterrupted()) {
                    //如果线程有中断标记则清除并返回true，否则false
                    System.out.println(threadName + "第一次interrupted" + Thread.interrupted());
                    System.out.println(threadName + "第二次interrupted" + Thread.interrupted());
                }
                System.out.println(threadName + " 已下单");
                int n = (2 + (int) (Math.random() * (6 - 2))) * 1000; // 2-5秒
                Thread.sleep(n);

                System.out.println(threadName + "已发货，耗时" + n + "天");

            } catch (InterruptedException e) {
                System.out.println(threadName + " 3天还不发货，别发了，退货吧");
            }

        }
    }

    static void interruptGroup() {
        // 当前threadGroup
        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
        // 线程数组
        Thread[] threads = new Thread[Thread.activeCount()];
        // 复制threadGroup中的线程到线程数组里面
        threadGroup.enumerate(threads);
        for (int i = 0; i < Thread.activeCount(); i++) {
            if (!threads[i].isDaemon()) {
                threads[i].interrupt();
            }
        }
    }


    public static void main(String[] args) throws InterruptedException {
        Runner runner = new Runner();
        DelayRunner delayRunner = new DelayRunner();
        Thread t1 = new Thread(runner, "普通线程");
        Thread t2 = new Thread(runner, "join的线程");
        Thread t3 = new Thread(delayRunner, "守护线程");
        t3.setDaemon(true);

        t1.start();
        //用户线程结束后，jvm退出，守护线程退出
        t3.start();
        //让步当前执行线程执行（可运行状态），可能没有效果
        Thread.yield();

        //下面属于main主线程
        for (int i = 0; i < 10; i++) {
            if (i == 5) {
                t2.start(); //启动t2线程
                //main线程调用了t2线程的join方法，导致main线程必须等待t2执行结束才可以向下执行
                t2.join();
            }
            System.out.println(Thread.currentThread().getName() + "====" + i);
        }
        for (int i = 0; i < 5; i++) {
            Threader threader = new Threader();
            threader.setName("中断发货测试" + i);
            threader.start();
        }
        Threader threader = new Threader();
        threader.setName("中断发货测试interrupt运行前处理");
        threader.start();
        threader.interrupt();
        Thread.sleep(3000);
        System.out.println("过去3天了...");
        //批量给当前用户线程设置中断标记
        interruptGroup();
    }
}
