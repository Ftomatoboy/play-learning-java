package com.concurrency.base.guava;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * 监听装饰器允许我们封装 ExecutorService 并在提交任务时返回 ListenableFuture 实例而不是简简单单的 Future 实例。
 * ListenableFuture 接口扩展自 Future 接口，并添加了一个新方法 addListener()，该方法用于添加在将来完成时调用的侦听器。
 *
 * 一般情况下，我们很少直接使用 ListenableFuture.addListener() 方法，而是使用 Futures 类提供的许多辅助方法。例如，通过Futures.allAsList() 方法，
 * 我们可以在单个 ListenableFuture 中组合多个 ListenableFuture 实例，并会在这些实例在成功完成后将所有的 futures 合并并返回结果。
 */
public class MonitorDecoratorExample {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 创建线程池
        ExecutorService executorService = Executors.newCachedThreadPool();
        // 包装
        ListeningExecutorService listeningExecutorService = MoreExecutors.listeningDecorator(executorService);

        // 利用线程池直接提交callable
        ListenableFuture<String> future1 = listeningExecutorService.submit(() -> "guava");
        ListenableFuture<String> future2 = listeningExecutorService.submit(() -> "hello world");
        // 合体！
        String greeting = Futures.allAsList(future1, future2).get() //获取两个的内容
                .stream() //转换成流
                .collect(Collectors.joining("->")); //用->拼接
        System.out.println(greeting);
    }
}
