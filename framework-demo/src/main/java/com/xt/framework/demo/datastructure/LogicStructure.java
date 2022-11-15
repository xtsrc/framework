package com.xt.framework.demo.datastructure;

import com.xt.framwork.common.core.util.ThreadPoolTools;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * @Author: tao.xiong
 * @Date: 2020/1/19 17:10
 * @Description: 线性结构：线性表、栈、队列、串、数组
 * 非线性结构：树、图
 */
@Slf4j
public class LogicStructure {
    private static final ExecutorService DEFAULT_EXECUTOR = ThreadPoolTools.defaultThreadPool;

    /**
     * 哈希值与低16位取异或（尽量打乱低16位数）
     */
    static int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    /**
     * ###############################currentHashMap############################
     * 并发问题的三个来源：原子性、可见性、有序性
     * ConcurrentHashMap 的线程安全指的是：ConcurrentHashMap只能保证提供的原子性读写操作是线程安全的。 也就是put()、get()操作是线程安全的。
     * 这两个操作对于多线程同时操作，线程之间是可见的，因为ConcurrentHashMap.Node.val和因为ConcurrentHashMap.baseCount被volatile修饰。
     * ConcurrentHashMap#putIfAbsent()，实现get()、put()原子性操作，因为 ConcurrentHashMap#putIfAbsent() 方法内部加了synchronized锁
     * <p>
     * Segment +table 分段锁：细化粒度
     * 锁:CAS+Synchronized取代Segment+ReentrantLock
     * 细粒度争抢可能性不高：ReentrantLock 不会自旋直接挂起。导致上下文切换开销
     */

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class User {
        /**
         * 用户名，也是Map的key
         */
        private String username;

        private int age;
    }

    static class UserService {

        private final Map<String, User> userMap = new ConcurrentHashMap<>();

        /**
         * 用户注册
         *
         * @param user 用户
         * @return 注册成功
         */
        boolean register(User user) {
            // User hasMapped = userMap.putIfAbsent(user.getUsername(), user);
            //get set 操作非原子性
            if (userMap.containsKey(user.getUsername())) {
                System.out.println("用户已存在");

                return false;
            } else {
                userMap.put(user.getUsername(), user);
                System.out.println("用户" + user.getUsername() + "_" + user.getAge() + "注册成功");

                return true;
            }
        }
    }

    /**
     * ########################################## Queue#####################################
     * ArrayBlockingQueue: 阻塞式的先进先出队列;只有一个ReentrantLock对象,生产者和消费者无法并行运行;必须指定队列大小
     * LinkedBlockingQueue:有两个锁,生产者和消费者可以并行;容量默认采用Integer.MAX_VALUE
     * PriorityBlockingQueue 按优先级排列的阻塞队列 内部只有一个Lock 默认的容量是11
     * SynchronousQueue 不会维护队列中元素的存储空间 每一个插入操作必须等待一个线程对应的移除操作 适用于生产者少消费者多的情况
     * DelayQueue 实现有序与延迟的效果
     */
    static class QueueService{
        int num;
        final BlockingQueue<String> strings = new ArrayBlockingQueue<>(1);
        Runnable producer = ()-> {
                    for  (int i=0;i<5;i++) {
                        String ele = "ele"+(++num);
                        try {
                            strings.put(ele);
                            log.info("ThreadName ->{} put ele->{}",Thread.currentThread().getName(),ele);
                        } catch (InterruptedException interruptedException) {
                            interruptedException.printStackTrace();
                            Thread.currentThread().interrupt();
                        }
                    }

        };

        Runnable consumer = () -> {
                for (int i=0;i<5;i++) {
                    try {
                        Thread.sleep(1*1000);
                        String take = strings.take();
                        log.info("ThreadName ->{} take ele->{}",Thread.currentThread().getName(),take);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                        Thread.currentThread().interrupt();
                    }
                }

        };
        final BlockingQueue<String> linkedStrings = new LinkedBlockingQueue<>(3);

        Runnable linkedProducer = ()-> {
            for  (int i=0;i<5;i++) {
                String ele = "ele"+(++num);
                try {
                    linkedStrings.put(ele);
                    log.info("ThreadName ->{} put ele->{}",Thread.currentThread().getName(),ele);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }

        };

        Runnable linkedConsumer = () -> {
            for (int i=0;i<5;i++) {
                try {
                    Thread.sleep(1*1000);
                    String take = linkedStrings.take();
                    log.info("ThreadName ->{} take ele->{}",Thread.currentThread().getName(),take);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }

        };
    }

    public static void main(String[] args) throws InterruptedException {
        //currentHashMap 测试用户注册
        UserService userService = new UserService();

        int threadCount = 8;

        ForkJoinPool forkJoinPool = new ForkJoinPool(threadCount);
        forkJoinPool.execute(() -> IntStream.range(0, threadCount)
                .mapToObj(i -> new User("张三", i))
                .parallel().forEach(userService::register));

        // 等待1s，否则看不到日志输出程序就结束了
        TimeUnit.SECONDS.sleep(1);


        //queue 测试
        QueueService queueService=new QueueService();
        //单生产单消费
        ThreadPoolTools.submitTask(queueService.producer, DEFAULT_EXECUTOR);
        ThreadPoolTools.submitTask(queueService.consumer, DEFAULT_EXECUTOR);
        //多生产多消费
        ThreadPoolTools.submitTask(queueService.linkedProducer, DEFAULT_EXECUTOR);
        ThreadPoolTools.submitTask(queueService.linkedProducer, DEFAULT_EXECUTOR);
        ThreadPoolTools.submitTask(queueService.linkedConsumer, DEFAULT_EXECUTOR);
        ThreadPoolTools.submitTask(queueService.linkedConsumer, DEFAULT_EXECUTOR);
        DEFAULT_EXECUTOR.shutdown();
    }
}
