package com.concurrency.base;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * ExecutorService 会自动提供一个线程池和相关 API，用于为其分配任务
 * 实例化ExecutorService 的方式有两种：一种是工厂方法，另一种是直接创建
 */
public class ExecutorServiceExample {
    public static void main(String[] args) {
        // 1. 创建ExecutorService 实例的最简单方法是使用 Executors 类的提供的工厂方法
        //工厂方法JDK官方文档 https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/Executors.html
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        // 2.直接创建 ExecutorService 的实例 以下示例与工厂方法 newSingleThreadExecutor() 的 源代码 非常相似 ，所以一般情况下不需要详细的手动配置
        new ThreadPoolExecutor(1,1,0L, TimeUnit.MICROSECONDS,new LinkedBlockingQueue<Runnable>());

        // 创建一个可运行的任务
        Runnable runnableTask = () -> {
            try {
                TimeUnit.MILLISECONDS.sleep(1000);
                System.out.println("execute: " + "runnable执行");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Callable<String> callableTask = () -> {
            TimeUnit.MILLISECONDS.sleep(1000);
            return "callable 相继执行";
        };

        List<Callable<String>> callableTasks = new ArrayList<>();
        callableTasks.add(callableTask);
        callableTasks.add(callableTask);
        callableTasks.add(callableTask);

        /**
         * execute(): 使用该方法没有任何可能获得任务执行结果或检查任务的状态
         * submit(): 会将一个 Callable 或 Runnable 任务提交给 ExecutorService 并返回 Future 类型的结果
         * invokeAny(): 将一组任务分配给 ExecutorService，使每个任务执行，并返回任意一个成功执行的任务的结果 ( 如果成功执行 )
         * invokeAll(): 将一组任务分配给 ExecutorService ，使每个任务执行，并以 Future 类型的对象列表的形式返回所有任务执行的结果
         */
        try {
            // 执行可运行的任务
            executorService.execute(runnableTask);

            /**
             * 执行可调用的任务列表，并获取 Future 对象以获取任务执行结果
             * Future 接口提供了一个特殊的阻塞方法 get()，它返回 Callable 任务执行的实际结果，但如果是 Runnable 任务，则只会返回 null
             * 因为get() 方法是阻塞的。如果调用 get() 方法时任务仍在运行，那么调用将会一直被执阻塞，直到任务正确执行完毕并且结果可用时才返回。
             * 执行的任务随时都可能抛出异常或中断执行。因此我们要将 get() 调用放在 try catch 语句块中，并捕捉 InterruptedException 或 ExecutionException 异常。
             * get() 的重载，get(超时的时间,时间的单位)
             */
            Future<String> submitFuture = executorService.submit(callableTask);
            System.out.println("submit:" + submitFuture.get());

            String invokeAnyResult = executorService.invokeAny(callableTasks);
            System.out.println("invokeAny:" + invokeAnyResult);

            List<Future<String>> invokeAllFutures = executorService.invokeAll(callableTasks);
            for (Future<String> future : invokeAllFutures) {
                System.out.println("invokeAll: 在300毫秒内" + future.get(300, TimeUnit.MICROSECONDS));
            }
            /**
             * Future的其他方法
             * boolean isDone      = future.isDone(); 检查已分配的任务是否已处理
             * boolean canceled    = future.cancel(true); 取消任务执行
             * boolean isCancelled = future.isCancelled(); 	检查任务是否已取消
             */
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        /**
         * 关闭线程池 关闭 ExecutorService 实例的最佳实战:
         * ExecutorService 首先停止执行新任务，等待指定的时间段完成所有任务。如果该时间到期，则立即停止执行。
         */
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }
}
/**
 * 尽管ExecutorService 相对简单，但仍有一些常见的陷阱
 * 1、保持未使用的ExecutorService存活；
 * 2、使用固定长度的线程池时设置了错误的线程池容量；
 * 使用ExecutorService 最重要的一件事，就是确定应用程序有效执行任务所需的线程数
 * 太大的线程池只会产生不必要的开销，只会创建大多数处于等待模式的线程。
 * 太少的线程池会让应用程序看起来没有响应，因为队列中的任务等待时间很长。
 * 3、在取消任务后调用Future的get()方法；
 * 尝试获取已取消任务的结果将触发 CancellationException 异常。
 * 4、使用Future的get()方法意外地阻塞了很长时间；
 * 应该使用超时来避免意外等待。
 */
