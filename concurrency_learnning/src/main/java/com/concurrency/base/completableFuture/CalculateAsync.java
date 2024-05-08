package com.concurrency.base.completableFuture;

import java.util.concurrent.*;

/**
 * 将 CompletableFuture 当作简单的 Future 使用
 *
 * 可以使用无任何参数的构造函数来创建此类的实例，用于表示未来的某些结果，然后将其交给使用者，并在将来的某个时间调用 complete() 方法完成
 * 消费者可以使用 get() 方法来阻止当前线程，直到提供此结果。
 */
public class CalculateAsync {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<Object> objectCompletableFuture = new CompletableFuture<>();
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(() -> {
            try {
                Thread.sleep(1000);
                objectCompletableFuture.complete("Hello");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        System.out.println(objectCompletableFuture.get());
        executorService.shutdown();
    }

    public Future<String> calculateAsync() throws InterruptedException, ExecutionException {
        Future<String> completableFuture = calculateAsync();
        //Future<String> completableFuture = CompletableFuture.completedFuture("Hello");
        // ...

        String result = completableFuture.get();
        System.out.println(result);

        return completableFuture;
    }
}
