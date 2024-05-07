package com.concurrency.base;

import java.util.concurrent.*;

/**
 * ScheduledExecutorService 接口用于在一些预定义的延迟之后运行任务和（ 或 ）定期运行任务。
 * 以下示例，只能单独打开一个测试
 * schedule() 方法允许在指定的延迟后执行一次任务
 * scheduleAtFixedRate() 方法允许在指定的初始延迟后执行任务，然后以一定的周期重复执行，其中 period 参数用于指定两个任务的开始时间之间的间隔时间，因此任务执行的频率是固定的。
 * scheduleWithFixedDelay() 方法类似于 scheduleAtFixedRate() ，它也重复执行给定的任务，但period 参数用于指定前一个任务的结束和下一个任务的开始之间的间隔时间。也就是指定下一个任务延时多久后才执行。执行频率可能会有所不同，具体取决于执行任何给定任务所需的时间。
 */
public class ScheduledExecutorServiceExample {
    public static void main(String[] args) throws InterruptedException {
        // 工厂类获取实例
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        // 创建线程
        Runnable runnableTask = () -> {
            //TimeUnit.MILLISECONDS.sleep(1000);
            System.out.println("runnable执行");
        };
        Callable<String> callableTask = () -> {
            TimeUnit.MILLISECONDS.sleep(1000);
            return "callable执行";
        };
        System.out.println("两秒后运行线程Task...");
        //在执行 callableTask 之前延迟了一秒钟。
        //scheduledExecutorService.schedule(runnableTask,2,TimeUnit.SECONDS);
        //scheduledExecutorService.schedule(callableTask,1,TimeUnit.SECONDS);

        // 在 100 毫秒的初始延迟后执行任务，之后，它将每 450 毫秒执行相同的任务
        //scheduledExecutorService.scheduleAtFixedRate(runnableTask,100,450,TimeUnit.MICROSECONDS);
        // 保证当前执行结束与另一个执行结束之间的暂停时间为 150 毫秒
        //scheduledExecutorService.scheduleWithFixedDelay(runnableTask, 100, 150, TimeUnit.MILLISECONDS);

        //创建了一个CountDownLatch实例，设置初始计数为3。CountDownLatch是一个同步辅助类，用于延迟线程的进程直到其他线程完成一定的操作。每次调用countDown()方法都会将计数减1，当计数达到0时，所有等待的线程被释放。
        CountDownLatch lock = new CountDownLatch(3);

        //定期任务开始
        ScheduledFuture<?> future = scheduledExecutorService.scheduleAtFixedRate(() -> {
            System.out.println("Hello World");
            //将计数减1
            lock.countDown();
        }, 500, 100, TimeUnit.MILLISECONDS);

        //使当前线程等待，直到锁存器计数到达零，或者指定的等待时间过去
        lock.await(1000, TimeUnit.MILLISECONDS);
        //取消定期任务
        future.cancel(true);
    }
}
