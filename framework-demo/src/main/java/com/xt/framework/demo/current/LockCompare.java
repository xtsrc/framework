package com.xt.framework.demo.current;


import com.xt.framwork.common.core.util.ThreadPoolTools;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.*;

/**
 * @Author: tao.xiong
 * @Date: 2020/4/10 19:44
 * @Description:
 */
public class LockCompare {
    /**

     */
    /**
     * 同步：JVM规范规定JVM基于进入和退出Monitor对象来实现方法同步和代码块同步
     * monitorenter指令是在编译后插入到同步代码块的开始位置，monitorexit是插入到方法结束处和异常处
     * <p>
     * 锁实现：硬件编码：CAS、TAS;
     * 锁状态：无锁状态，偏向锁状态，轻量级锁状态和重量级锁状态；锁可以升级但不能降级，提高获得锁和释放锁的效率
     * <p>
     * 偏向性锁：获取：记录theadId 减少CAS操作；撤销：有竞争才释放| 只有一个线程进入临界区
     * 自旋锁： for(;;) 忙等+次数
     * 轻量级锁：获取：CAS->自旋->失败变成重量级锁；解锁：CAS |多个线程交替进入临界区
     * 重量级锁：对象内部的一个叫做监视器锁（monitor）来实现的，依赖操作系统底层Mutex Lock |多线程同时进入临界区
     * <p>
     * 锁分类：自旋锁 ，自旋锁的其他种类，阻塞锁，可重入锁 ，读写锁 ，互斥锁 ，悲观锁 ，乐观锁 ，公平锁 ，偏向锁， 对象锁，线程锁，锁粗化， 锁消除，轻量级锁，重量级锁， 信号量，独享锁，共享锁，分段锁
     * <p>
     * <p>
     * 1.synchronized同步锁 :锁同步代码块;锁方法;可用object.wait() object.notify()来操作线程等待唤醒;JVM层面上实现的,代码执行时出现异常，JVM会自动释放锁定;非公平，悲观，独享，互斥，可重入的重量级锁
     * 2.lock:代码实现的，要保证锁定一定会被释放，就必须将unLock()放到finally{}中
     * 3.ReentrantLock 默认非公平但可实现公平的，悲观，独享，互斥，可重入，重量级锁。
     * 4.ReentrantReadWriteLocK:默认非公平但可实现公平的，悲观，写独享，读共享，读写，可重入，重量级锁。
     */

    class TaskQueue {
        Queue<String> queue = new LinkedList<>();

        private int count;
        private int[] counts = new int[10];

        // 尝试获取this锁，超时（1s）后返回false
        private final Lock lock = new ReentrantLock();

        private final Condition condition = lock.newCondition();

        //一个线程写入，没有写入时多个线程读取；适合读多写少
        private final ReadWriteLock rwlock = new ReentrantReadWriteLock();
        private final Lock rlock = rwlock.readLock();
        private final Lock wlock = rwlock.writeLock();

        // 竞争this锁
        public synchronized void addTask(String s) {
            this.queue.add(s);
            // 唤醒等到this锁线程
            this.notifyAll();
            // 执行后，多个线程时可能只会唤醒一个，其他继续等待
        }

        // 竞争this锁
        public synchronized String getTask() throws InterruptedException {
            // 不能用if：多个线程同时唤醒时，只有一个线程得到this锁，执行remove后队列空，报错；
            while (queue.isEmpty()) {
                // 释放this锁
                this.wait();// 必须在synchronized中调用
                // 得到this锁
            }
            return queue.remove();
        }

        public void add(int n) {
            lock.lock();
            try {
                count += n;
            } finally {
                lock.unlock();
            }
        }

        public void addTask2(String s) {
            lock.lock();
            try {
                queue.add(s);
                condition.signalAll();
            } finally {
                lock.unlock();
            }
        }

        public String getTask2() throws InterruptedException {
            lock.lock();
            try {
                while (queue.isEmpty()) {
                    condition.await();
                }
                return queue.remove();
            } finally {
                lock.unlock();
            }
        }

        public void inc(int index) {
            wlock.lock(); // 加写锁
            try {
                counts[index] += 1;
            } finally {
                wlock.unlock(); // 释放写锁
            }
        }

        public int[] get() {
            rlock.lock(); // 加读锁
            try {
                return Arrays.copyOf(counts, counts.length);
            } finally {
                rlock.unlock(); // 释放读锁
            }
        }

    }

    public class Point {
        //乐观锁，读时允许写
        private final StampedLock stampedLock = new StampedLock();

        private double x;
        private double y;

        public void move(double deltaX, double deltaY) {
            long stamp = stampedLock.writeLock(); // 获取写锁
            try {
                x += deltaX;
                y += deltaY;
            } finally {
                stampedLock.unlockWrite(stamp); // 释放写锁
            }
        }

        public double distanceFromOrigin() {
            long stamp = stampedLock.tryOptimisticRead(); // 获得一个乐观读锁
            // 注意下面两行代码不是原子操作
            // 假设x,y = (100,200)
            double currentX = x;
            // 此处已读取到x=100，但x,y可能被写线程修改为(300,400)
            double currentY = y;
            // 此处已读取到y，如果没有写入，读取是正确的(100,200)
            // 如果有写入，读取是错误的(100,400)
            if (!stampedLock.validate(stamp)) { // 检查乐观读锁后是否有其他写锁发生
                stamp = stampedLock.readLock(); // 获取一个悲观读锁
                try {
                    currentX = x;
                    currentY = y;
                } finally {
                    stampedLock.unlockRead(stamp); // 释放悲观读锁
                }
            }
            return Math.sqrt(currentX * currentX + currentY * currentY);
        }
    }


    /**
     * condition ：full的signal方法只会唤起调用full.await()的线程；empty的signal方法只会唤起调用empty.await()的线程（也就是Add类线程），
     * 保证了消费者只会唤起生产者，生产者只会唤起消费者。
     * 替代方案：BlockingQueue
     */
    // 锁
    private static final Lock lock = new ReentrantLock();

    // pool中有数据时的条件
    private static final Condition full = lock.newCondition();

    // pool中没有数据时的条件
    private static final Condition empty = lock.newCondition();

    // 模拟一个缓冲区，可以看到缓冲区是不可并发的
    private static final List<String> pool = new LinkedList<>();

    // 生成add对象的唯一编号
    private static final AtomicInteger addThreadNumber = new AtomicInteger(0);

    // 生成remove对象的唯一编号
    private static final AtomicInteger removeThreadNumber = new AtomicInteger(0);

    // 生产的数据编号
    private static final AtomicLong atomicLong = new AtomicLong(0);

    static class Add implements Runnable {

        @Override
        public void run() {
            int number = addThreadNumber.addAndGet(1);
            int i = 10;
            while (i-- != 0) {
                lock.lock();
                try {
                    System.out.printf("%s%s 获取了锁%n", "Add", number);
                    // 这里可以减缓一下运行速度，方便debug
                    try {
                        Thread.sleep((long) (Math.random() * 1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Thread.currentThread().interrupt();
                    }
                    try {
                        while (!pool.isEmpty()) {
                            System.out.printf("%s%s 等待池子变空%n", "Add", number);
                            empty.await();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Thread.currentThread().interrupt();
                    }

                    pool.add(String.format("Add线程%s 创建了 %s 号物品", number, atomicLong.addAndGet(1)));

                    full.signal();
                    System.out.printf("%s%s 发送已经变满%n", "Add", number);
                } finally {
                    lock.unlock();
                    System.out.printf("%s%s 释放了锁%n", "Add", number);
                }

            }
        }
    }

    static class Remove implements Runnable {

        @Override
        public void run() {
            int number = removeThreadNumber.addAndGet(1);
            while (true) {
                lock.lock();
                try {
                    System.out.printf("%s%s 获取了锁%n", "Remove", number);
                    // 这里可以减缓一下运行速度，方便debug
                    try {
                        Thread.sleep((long) (Math.random() * 1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    try {
                        while (pool.size() == 0) {
                            System.out.printf("%s%s 等待池子变满%n", "Remove", number);
                            full.await();
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println((String.format("Remove线程%s 获取了 %s", number, pool.remove(0))));

                    empty.signal();
                    System.out.printf("%s%s 发送可能变空信号%n", "Remove", number);
                } finally {
                    lock.unlock();
                    System.out.printf("%s%s 释放了锁%n", "Remove", number);
                }

            }
        }
    }

    public static void main(String[] args) {
        // 这里可以模拟各种情况
        // 例如1:1/1:N/N:1/N:N 的生产者消费者数量
        for (int i = 0; i < 1; i++) {
            ThreadPoolTools.defaultThreadPool.execute(new Add());
        }
        for (int i = 0; i < 10; i++) {
            ThreadPoolTools.defaultThreadPool.execute(new Remove());
        }
    }
}
