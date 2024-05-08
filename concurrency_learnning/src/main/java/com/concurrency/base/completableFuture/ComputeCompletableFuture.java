package com.concurrency.base.completableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 用于封装计算逻辑的 CompletableFuture
 *
 * CompletableFuture 的静态方法
 *
 * 创建：
 * runAsync() 和 supplyAsync() 允许我们从 Runnable 和 Supplier 中创建 CompletableFuture 实例。 Runnable 和 Supplier 都是功能接口，由 Java 8 的新功能，可以将它们的实例作为lambda 表达式传递：
 *      Runnable 接口与线程中使用的旧接口相同，不允许返回值
 *      Supplier 接口是一个通用的功能接口，只有一个没有参数的方法，并返回一个参数化类型的值
 *
 * 计算：
 * thenApply() :    用途: 用于对异步操作的结果进行转换或处理，并返回一个新的值。
 *                  返回类型: 返回一个新的CompletableFuture，其中包含通过函数应用于原始结果得到的值。
 *
 * thenAccept():    用途: 用于处理异步操作的结果，但不需要返回值。常用于消费处理，如打印或保存结果。
 *                  返回类型: 返回一个CompletableFuture<Void>，因为它不产生新的计算结果，只对结果进行消费。
 *
 * thenRun():       用途: 用于执行一些最终操作，如清理或关闭资源，这些操作不需要使用到异步操作的结果。
 *                  返回类型: 返回一个CompletableFuture<Void>，因为它不操作或产生计算结果。
 */
public class ComputeCompletableFuture {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // runAsync() 和 supplyAsync()
        CompletableFuture<String> stringCompletableFuture1 = CompletableFuture.supplyAsync(() -> "hello");//Supplier
        CompletableFuture.runAsync(() -> System.out.println("world"));//Runnable
        System.out.println("supplyAsync(): " + stringCompletableFuture1.get());

        CompletableFuture<String> hello = CompletableFuture.completedFuture("hello");

        // thenApply()
        CompletableFuture<String> thenApply = hello.thenApply(s -> s + " world");
        System.out.println("thenApply():" + thenApply.get());

        // thenAccept()
        CompletableFuture<Void> voidCompletableFuture = hello.thenAccept(s -> System.out.println("Computation returned: " + s));
        System.out.println("thenAccept(): " + voidCompletableFuture.get());

        // thenRun()
        CompletableFuture<Void> future = hello.thenRun(() -> System.out.println("计算完成"));
        System.out.println("thenRun(): " + future.get());
    }
}
