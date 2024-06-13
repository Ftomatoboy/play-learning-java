package com.streams;

import java.util.stream.IntStream;

public class IntStreamDemo {
    public static void main(String[] args) {
        /**
         * IntStream.rangeClosed(13, 15) 创建一个从 13 到 15（包括 15）的连续整数流
         */
        System.out.println("--Using IntStream.rangeClosed--");
        IntStream.rangeClosed(13, 15)
                .map(n -> n * n) // 将每个元素平方
                .forEach(s -> System.out.print(s + " ")); // 打印每个平方值
        // 输出: 169 196 225

        /**
         * IntStream.range(13, 15) 创建一个从 13 到 14（不包括 15）的连续整数流
         */
        System.out.println("\n--Using IntStream.range--");
        IntStream.range(13, 15)
                .map(n -> n * n) // 将每个元素平方
                .forEach(s -> System.out.print(s + " ")); // 打印每个平方值
        // 输出: 169 196

        /**
         * IntStream.rangeClosed(1, 10) 创建一个从 1 到 10（包括 10）的连续整数流
         */
        System.out.println("\n--Sum of range 1 to 10--");
        System.out.print(IntStream.rangeClosed(1, 10).sum());
        // 输出: 55

        /**
         * .sorted() 对流中的元素进行排序
         */
        System.out.println("\n--Sorted number--");
        IntStream.of(13, 4, 15, 2, 8)
                .sorted() // 对流中的元素进行排序
                .forEach(s -> System.out.print(s + " ")); // 打印每个元素
        // 输出: 2 4 8 13 15
    }
}
