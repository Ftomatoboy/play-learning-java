package com.concurrency.base.forkJoinPool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class CustomRecursiveTask extends RecursiveTask<Integer> {
    private int[] arr;

    private static final int THRESHOLD = 20;

    public CustomRecursiveTask(int[] arr) {
        this.arr = arr;
    }

    @Override
    protected Integer compute() {
        if (arr.length > THRESHOLD) {
            return ForkJoinTask.invokeAll(createSubtasks())
                    .stream()
                    .mapToInt(ForkJoinTask::join)
                    .sum();
        } else {
            return processing(arr);
        }
    }

    private Collection<CustomRecursiveTask> createSubtasks() {
        List<CustomRecursiveTask> dividedTasks = new ArrayList<>();
        dividedTasks.add(new CustomRecursiveTask(
                Arrays.copyOfRange(arr, 0, arr.length / 2)));
        dividedTasks.add(new CustomRecursiveTask(
                Arrays.copyOfRange(arr, arr.length / 2, arr.length)));
        return dividedTasks;
    }

    private Integer processing(int[] arr) {
        return Arrays.stream(arr)
                .filter(a -> a > 10 && a < 27)
                .map(a -> a * 10)
                .sum();
    }

    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 11, 12, 13, 24, 25, 26, 27, 28};
        CustomRecursiveTask customRecursiveTask = new CustomRecursiveTask(arr);
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        // 正确地提交任务到线程池并等待结果
        int result = forkJoinPool.invoke(customRecursiveTask);
        System.out.println("The sum is: " + result);

        forkJoinPool.shutdown();
    }

}
