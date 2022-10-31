package com.xt.framwork.common.core.util;

import com.alibaba.ttl.threadpool.TtlExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.core.task.TaskExecutor;

import javax.annotation.Resource;
import java.util.concurrent.*;
import java.util.function.Supplier;

import static java.util.concurrent.Executors.*;

/*import qunar.concurrent.SharedExecutor;*/

/**
 * @Author: tao.xiong
 * @Date: 2019/11/15 11:47
 * @Description: tool for thread pool
 */

/**
 * 提交任务->核心线程池已满（corePoolSize）->等待队列已满(waitQueen)->线程池已满(maximumPoolSize)->拒绝策略（RejectedExecutionHandler）
 */
public final class ThreadPoolTools {
    /**
     * cpu密集型任务：过多线程会增加切换上下文的次数，一般为cpu核心数+1
     */
    public static final int CPU_CORE_POOL_SIZE = 5;
    /**
     * IO密集型任务：一般为2*CPU核心数
     */
    public static final int IO_CORE_POOL_SIZE = 8;

    /**
     * 最大线程数
     */
    public static final int MAXIMUM_POOL_SIZE = 8;

    /**
     * 等待队列大小，考虑任务提交、任务执行效率，综合业务时效性设置
     */
    private static final int WAIT_QUEUE_SIZE = 10;

    /**
     * 允许线程空闲的时间，考虑任务提交频率设置
     */
    public static final int KEEP_ALIVE_TIME = 1;

    // 阻塞队列

    /**
     * 基于数组的先进先出队列，有界
     */
    public static BlockingQueue<Object> arrayBlockingQueue = new ArrayBlockingQueue<>(WAIT_QUEUE_SIZE);

    /**
     * 基于链表的先进先出队列，无界
     */
    public static BlockingQueue<Object> linkedBlockingQueue = new LinkedBlockingQueue<>();

    /**
     * 无缓冲的等待队列，无界，直接新建线程处理，达到maximumPoolSize会报错，一般设为无限大
     */
    public static BlockingQueue<Runnable> synchronousQueue = new SynchronousQueue<>();

    /**
     * 延迟队列，无界，必须实现delayed接口，先入队，达到指定延时时间执行
     */
    public static BlockingQueue<Delayed> delayQueue = new DelayQueue<>();

    /**
     * 优先权阻塞队列
     */
    public static BlockingQueue<Object> priorityBlockingQueue = new PriorityBlockingQueue<>();

    // 拒绝策略
    /**
     * 默认，队列满了就丢弃抛出异常
     */
    public static RejectedExecutionHandler abortPolicy = new ThreadPoolExecutor.AbortPolicy();

    /**
     * 队列满了丢弃任务不抛出异常
     */
    public static RejectedExecutionHandler discardPolicy = new ThreadPoolExecutor.DiscardPolicy();

    /**
     * 删除最早进入队列的任务，之后尝试加入队列
     */
    public static RejectedExecutionHandler discardOldestPolicy = new ThreadPoolExecutor.DiscardOldestPolicy();

    /**
     * 加入队列失败，主线程自己去执行该任务
     */
    public static RejectedExecutionHandler callerRunsPolicy = new ThreadPoolExecutor.CallerRunsPolicy();

    /**
     * 自定义拒绝策略：阻塞提交，队列满了阻塞主线程提交
     */

    public static RejectedExecutionHandler blockingRetryPolicy = new BlockingRetryPolicy();

    public static class BlockingRetryPolicy implements RejectedExecutionHandler {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            try {
                // 核心改造点：由blockingqueue的offer改成put阻塞方法
                if (!executor.isShutdown()) {
                    executor.getQueue().put(r);
                }
            } catch (InterruptedException ignored) {
            }
        }
    }

    // 线程池

    /**
     * 固定大小 无界队列 OOM
     */
    public static ExecutorService fixedThreadPool = newFixedThreadPool(10);
    /**
     * 有缓存的线程池 可无限创建线程 OOM
     */
    public static ExecutorService cachedThreadPool = newCachedThreadPool();
    /**
     * 可调度的线程池 无限创建线程 OOM
     */
    public static ScheduledExecutorService scheduledThreadPool = newScheduledThreadPool(2);
    /**
     * 单线程的线程池 无界队列 OOM
     */
    public static ExecutorService singleThreadExecutor = newSingleThreadExecutor();
    /**
     * 默认线程池
     */
    public static ExecutorService defaultThreadPool = new ThreadPoolExecutor(CPU_CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
            KEEP_ALIVE_TIME, TimeUnit.SECONDS, synchronousQueue,
            new ThreadFactoryBuilder().setNameFormat("thread-%d").build(), blockingRetryPolicy);

    /**
     * spring管理的线程池
     */
    @Resource(name = "taskExecutor")
    public TaskExecutor taskExecutor;

    /**
     * 线程池管理，线程之间互不影响
     */
   /* public SharedExecutor sharedExecutor = new SharedExecutor("test", WAIT_QUEUE_SIZE, CPU_CORE_POOL_SIZE,
            MAXIMUM_POOL_SIZE, blockingRetryPolicy);*/

    /**
     * 允许线程池父子线程对象复用
     * 父子线程提交同一个线程池执行拒绝策略阻塞时会导致循环等待死锁
     */
    public static ExecutorService ttlExecutorService = TtlExecutors.getTtlExecutorService(defaultThreadPool);

    // 任务提交

    /**
     * @param runnable 无返回值
     */
    public static void submitTask(Runnable runnable, ExecutorService executorService) {
        executorService.submit(runnable);
    }

    /**
     * @param callable 有返回值 阻塞（get）或轮询（isDone）获取
     */
    public static <T> Future<T> submitTask(Callable<T> callable, ExecutorService executorService) {
        return executorService.submit(callable);
    }

    /**
     * 无返回值 传入回调函数 多个可串行或并行，合并等
     */
    public static CompletableFuture<Void> submitAsyncTask(Runnable runnable, ExecutorService executorService) {
        return CompletableFuture.runAsync(runnable, executorService);
    }

    /**
     * 有返回值
     */
    public static <U> CompletableFuture<U> submitAsyncTask(Supplier<U> supplier, ExecutorService executorService) {
        return CompletableFuture.supplyAsync(supplier, executorService);
    }
}
