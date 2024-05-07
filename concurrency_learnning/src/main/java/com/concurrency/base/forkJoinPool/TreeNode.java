package com.concurrency.base.forkJoinPool;

import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

public class TreeNode {

    int value;
    Set<TreeNode> children;

    // 构造函数
    TreeNode(int value, TreeNode... children) {
        this.value = value;
        // 将数组转换为HashSet
        this.children = new HashSet<>(Arrays.asList(children));
    }
}