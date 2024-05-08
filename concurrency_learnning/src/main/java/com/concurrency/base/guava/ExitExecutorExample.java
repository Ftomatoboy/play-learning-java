package com.concurrency.base.guava;

import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *  在线程池仍在运行其任务时关闭虚拟机: 即使采用了取消机制，也无法保证任务执行良好，并在执行程序服务 （ Executor ）关闭时停止工作。这可能会导致 JVM 在任务继续工作时无限期挂起。
 *  Guava 引入了一系列已经实例化好的执行器 （ Executor ） 服务。它们是守护线程模式，但会与 JVM 一起终止。
 *  Runtime.getRuntime().addShutdownHook() 方法用于添加一个关闭钩子，用于设置 VM 在放弃挂起的任务之前等待一段预配置的超时时间。
 *
 *  以下代码为线程池使用中，主线程关闭虚拟机，触发钩子优雅关闭线程池中的线程
 */
public class ExitExecutorExample {
    public static void main(String[] args) {
        // 创建固定线程池
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
        // 包装线程池，确保JVM关闭时，让线程池中的任务执行完毕再退出。超时时间100ms，意味着在JVM关闭后100ms内任务未完成则尝试中断
        ExecutorService executorService = MoreExecutors.getExitingExecutorService(executor, 100, TimeUnit.MILLISECONDS);
        // 执行死循环任务
        executorService.submit(() -> {
            try {
                // 判断线程是否中断
                while (!Thread.currentThread().isInterrupted()) {
                    System.out.println("循环执行任务中...");
                    Thread.sleep(100);  // 添加 sleep 以降低循环速度，并允许响应中断
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();  // 保持中断状态
                System.out.println("任务被中断");
            }
        });

        // 注册JVM关闭后的钩子
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // 作为保护线程开始标志，主线程关闭后，触发钩子函数关闭线程池
            System.out.println("JVM Shutdown Hook is running");
            // 关闭线程池
            executorService.shutdown();
            try {
                // 等待线程池完全终止，最多等待一定时间
                if (!executorService.awaitTermination(1, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("线程池已关闭");
        }));

        // 主线程休眠一段时间，以模拟应用运行并触发关闭
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        // 模拟应用关闭
        System.exit(0);
    }
}
