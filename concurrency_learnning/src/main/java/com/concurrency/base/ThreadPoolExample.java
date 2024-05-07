package com.concurrency.base;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Executors、Executor 和 ExecutorService
 * Executors 是一个帮助类，提供了创建几种预配置线程池实例的方法
 * Executor 和 ExecutorService 接口则用于与 Java 中不同线程池的实现协同工作
 *
 * ThreadPoolExecutor 是一个可被继承 ( extends ) 的线程池实现，包含了用于微调的许多参数和钩子
 * ThreadPoolExecutor 创建的线程池由固定数量的核心线程组成，这些线程在 ThreadPoolExecutor 生命周期内始终存在，除此之外还有一些额外的线程可能会被创建，并会在不需要时主动销毁
 * 1、 corePoolSize: 用于指定在线程池中实例化并保留的核心线程数。如果所有核心线程都忙，并且提交了更多任务，则允许线程池增长到 maximumPoolSize
 * 2、 maximumPoolSize；
 * 3、 keepAliveTime: 额外的线程（ 即，实例化超过 corePoolSize 的线程 ）在空闲状态下的存活时间
 *
 * Executors.newFixedThreadPool()：固定线程池，如果同时运行的任务的数量始终小于或等于参数，那么这些任务会立即执行。否则，其中一些任务可能会被放入队列中等待轮到它们。
 * Executors.newSingleThreadExecutor()：单线程线程池，所有的任务都按顺序执行，使用了不可变包装器进行修饰，因此在创建后无法重新配置
 * Executors.newCachedThreadPool()：缓存线程池，该方法创建的线程池没有任何核心线程，因为它将 corePoolSize 属性设置为 0，但同时有可以创建最大数量的额外线程，因为它将 maximumPoolSize 设置为 Integer.MAX_VALUE，
 *                                  且将 keepAliveTime 的值设置为 60 秒。这些参数值意味着缓存的线程池可以无限制地增长以容纳任何数量的已提交任务。但是，当不再需要线程时，它们将在 60秒不活动后被销毁。
 */
public class ThreadPoolExample {
    private ThreadPoolExecutor executor;
    private int poolSize;

    public ThreadPoolExample(int poolSize, int queueCapacity) {
        this.poolSize = poolSize;
        this.executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(poolSize); //固定线程池
        //this.executor = (ThreadPoolExecutor) Executors.newCachedThreadPool(); //缓存线程池
    }
    public void submitTask(Runnable task) throws InterruptedException {
        this.executor.submit(task);
    }
    // 当前在线程池中活跃的线程数量
    public int getPoolSize() {
        return this.executor.getPoolSize();
    }
    // 获取线程池等待执行任务的队列数量
    public int getQueueSize() {
        return this.executor.getQueue().size();
    }

    public void shutdown() {
        this.executor.shutdown();
    }
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ThreadPoolExample pool = new ThreadPoolExample(2, 1);

        pool.submitTask(() -> {
            try {
                System.out.println("任务1执行了");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        pool.submitTask(() -> {
            try {
                System.out.println("任务2执行了");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        pool.submitTask(() -> {
            try {
                System.out.println("任务3执行了");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // 确保主线程等待足够时间让线程池状态稳定，固定线程池状态下，三个任务执行需要两秒
        Thread.sleep(100);
        //Thread.sleep(1000);
        //Thread.sleep(2000);

        System.out.println("当前在线程池中活跃的线程数量: " + pool.getPoolSize());
        System.out.println("当前任务的积压数量: " + pool.getQueueSize());

        pool.shutdown();

        //使用 Runnable 作为参数的方法不会抛出异常也不会返回任何值 ( 返回 void )
        AtomicInteger counter = new AtomicInteger();
        ExecutorService executorService1 = Executors.newSingleThreadExecutor();
        executorService1.execute(()-> System.out.println("Hello World!"));
        executorService1.submit(() -> {
            counter.set(1);
        });
        System.out.println(counter.get());
        executorService1.submit(() -> {
            counter.compareAndSet(1, 2);
        });
        System.out.println(counter.get());
        executorService1.shutdown();
    }

}
