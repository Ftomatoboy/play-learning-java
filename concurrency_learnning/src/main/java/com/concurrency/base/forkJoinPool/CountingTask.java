package com.concurrency.base.forkJoinPool;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

/**
 * 基于Java Fork/Join框架的并行计算示例，用于计算一棵树中所有节点的值的总和。这里使用了RecursiveTask，这是Fork/Join框架的一部分，用于有返回值的任务。
 */
public class CountingTask extends RecursiveTask<Integer> {

    private final TreeNode node;

    // 构造器接收树的一个节点
    public CountingTask(TreeNode node) {
        this.node = node;
    }

    @Override
    protected Integer compute() {
        // 将当前节点的值加上所有子节点值的和
        return node.value + node.children.stream()
                .map(childNode -> new CountingTask(childNode).fork())// 为每个子节点创建一个新的任务并执行
                .collect(Collectors.summingInt(ForkJoinTask::join));// 收集结果并求和
    }

    public static void main(String[] args) {
        // 创建树
        TreeNode tree = new TreeNode(5,
                new TreeNode(3), new TreeNode(2,
                new TreeNode(2), new TreeNode(8)));

        // 使用通用的ForkJoinPool来执行任务
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        // 计算并获取结果
        int sum = forkJoinPool.invoke(new CountingTask(tree));
        System.out.println("Total sum of all nodes: " + sum);
    }
}
