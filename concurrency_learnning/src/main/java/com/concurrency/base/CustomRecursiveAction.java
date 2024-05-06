package com.concurrency.base;

import java.util.concurrent.RecursiveAction;

/**
 * fork/join 框架
 * ForkJoinPool 是 fork/join 框架的核心，是 ExecutorService 的一个实现，用于管理工作线程，并提供了一些工具来帮助获取有关线程池状态和性能的信息。初始化方法ForkJoinPool.commonPool();
 * ForkJoinTask 是 ForkJoinPool 线程之中执行的任务的基本类型。我们日常使用时，一般不直接使用 ForkJoinTask ，而是扩展它的两个子类中的任意一个
 * 1、 任务不返回结果(返回void）的RecursiveAction；
 *
 * 这两个类都有一个抽象方法 compute() ，用于定义任务的逻辑。
 * 我们所要做的，就是继承任意一个类，然后实现 compute() 方法。
 */
//@Slf4g
public class CustomRecursiveAction extends RecursiveAction {
    private String workload = "";
    private static final int THRESHOLD = 4;

    //private static Logger logger = Logger.getAnonymousLogger();

    public CustomRecursiveAction(String workload) {
        this.workload = workload;
    }

    @Override
    protected void compute() {

    }
}
