package com.concurrency.base;

import java.util.concurrent.*;

/**
 * ScheduledExecutorService 接口用于在一些预定义的延迟之后运行任务和（ 或 ）定期运行任务。
 * 以下示例，只能单独打开一个测试
 */
public class ScheduledExecutorServiceExample {
    public static void main(String[] args) {
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
        scheduledExecutorService.scheduleWithFixedDelay(runnableTask, 100, 150, TimeUnit.MILLISECONDS);
    }
}
