package com.concurrency.base.guava;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * 在同一个线程中执行任务
 */
public class GuavaExecutorExample {

    public static void main(String[] args) {
        Executor executor = MoreExecutors.directExecutor(); //静态单例,开销小
        AtomicBoolean executed = new AtomicBoolean();
        //CountDownLatch latch = new CountDownLatch(1);

        executor.execute(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            executed.set(true);
            //latch.countDown();
        });

        //latch.await(); // 等待直到 latch 计数为0
        System.out.println(executed.get());
    }
}
