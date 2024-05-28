package com.streams;

import java.util.ArrayList;
import java.util.stream.Stream;

public class CountLongWords {
    public static void main(String[] args) {
        ArrayList<Integer> integers = new ArrayList<>();
        integers.add(1);
        integers.add(2);
        integers.add(3);
        integers.add(4);
        integers.add(5);
        integers.add(6);
        integers.add(7);

        int count = 0;
        for (Integer i : integers){
            if (i < integers.size()) count++;
        }
        System.out.println("正常操作" + count);

        // stream 流操作 filter对stream对象进行转换 count方法是种植操作
        //long count = integers.stream().filter(w -> w < integers.size()).count();
        Stream<Integer> integerStream = integers.stream().filter(w -> w < integers.size());
        long counts = integerStream.count();
        System.out.println("流操作" + counts);
    }
}
