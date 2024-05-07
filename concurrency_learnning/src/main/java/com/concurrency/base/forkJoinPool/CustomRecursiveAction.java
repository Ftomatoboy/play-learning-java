package com.concurrency.base.forkJoinPool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * fork/join 框架
 * ForkJoinPool 是 fork/join 框架的核心，是 ExecutorService 的一个实现，用于管理工作线程，并提供了一些工具来帮助获取有关线程池状态和性能的信息。初始化方法ForkJoinPool.commonPool();
 * ForkJoinTask 是 ForkJoinPool 线程之中执行的任务的基本类型。我们日常使用时，一般不直接使用 ForkJoinTask ，而是扩展它的两个子类中的任意一个
 * 1、 任务不返回结果(返回void）的RecursiveAction；
 * 2、 返回值的任务的RecursiveTask<V>；
 * 这两个类都有一个抽象方法 compute() ，用于定义任务的逻辑。
 * 我们所要做的，就是继承任意一个类，然后实现 compute() 方法。
 */
public class CustomRecursiveAction extends RecursiveAction {
    private String workload = "";
    private static final int THRESHOLD = 4;

    private static Logger logger = Logger.getAnonymousLogger();

    public CustomRecursiveAction(String workload) {
        this.workload = workload;
    }

    @Override
    protected void compute() {
        if (workload.length() > THRESHOLD) {
            ForkJoinTask.invokeAll(createSubtasks());
        } else {
            processing(workload);
        }
    }

    //将当前工作负载一分为二，生成两个新的 CustomRecursiveAction 对象，每个对象处理一半的字符串。
    private List<CustomRecursiveAction> createSubtasks() {
        List<CustomRecursiveAction> subtasks = new ArrayList<>();

        // 更精确的分割避免可能的非均等分割
        int mid = workload.length() / 2;
        String partOne = workload.substring(0, mid);
        String partTwo = workload.substring(mid);

        subtasks.add(new CustomRecursiveAction(partOne));
        subtasks.add(new CustomRecursiveAction(partTwo));

        return subtasks;
    }
    // 输入字符串转换为大写，并通过日志记录处理
    private void processing(String work) {
        String result = work.toUpperCase();
        System.out.println("This result - (" + result + ") - was processed by "
                + Thread.currentThread().getName());
    }

    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        forkJoinPool.execute(new CustomRecursiveAction("hello world"));
        forkJoinPool.shutdown();

        // 确保主线程延迟关闭，直到所有任务完成
        try {
            forkJoinPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
