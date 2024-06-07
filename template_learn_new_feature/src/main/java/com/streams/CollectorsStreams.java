package com.streams;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CollectorsStreams {
    public static void main(String[] args) {
        /**
         * Java 所有集合的 stream().collect() 可以接受一个收集器实例作为其参数并返回该收集器的计算结果
         */
        List<Integer> list = Arrays.asList(1,2,3,4);
        /**
         * Collectors.averagingDouble(d->d*2) 对每个元素执行 *2 操作后计算平均值
         */
        Double result = list.stream().collect(Collectors.averagingDouble(d->d*2));
        System.out.println("averagingDouble："+result);
        /**
         * Collectors.averagingInt() 方法和 Collectors.averagingDouble() 一样，不同的是它把流中的所有元素看成是 int 类型，并返回一个浮点类型的平均值
         */
        Double collect = list.stream().collect(Collectors.averagingInt(v -> v * 2));
        System.out.println("averagingInt: "+collect);
        /**
         * Collectors.collectingAndThen() 函数应该最像 map and reduce 了，它可接受两个参数，第一个参数用于 reduce 操作，而第二参数用于 map 操作
         * 也就是，先把流中的所有元素传递给第二个参数，然后把生成的集合传递给第一个参数来处理
         */
        Double collect1 = list.stream().collect(Collectors.collectingAndThen(Collectors.averagingLong(v -> v * 2), s -> s * s));
        System.out.println("collectingAndThen: "+collect1);
    }
}
