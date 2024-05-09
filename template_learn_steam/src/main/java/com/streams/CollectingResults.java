package com.streams;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * 收集结果
 * iterator 迭代器
 * forEach 将某个函数应用到每个元素
 * toArray 返回一个泛型数组
 * collect 针对流中的元素收集到另一个目标中 他会接收一个Collectors接口
 * mapToInt
 */
public class CollectingResults {
    public static Stream<String> noVowels() throws IOException {
        Path path = Paths.get("../gutenberg/alice30.txt");
        String contents = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        Stream<String> words = Stream.of(contents.split("\\PL+"));
        return words.map(s -> s.replaceAll("爹", "爷"));
    }

    public static <T> void show(String label, Set<T> set){
        System.out.print(label + set.getClass().getName());
        System.out.println("["
                + set.stream().limit(10).map(Object :: toString).collect(Collectors.joining(", "))
                + "]");
    }

    public static void main(String[] args) throws IOException {
        // 获取当前流中元素的迭代器
        Iterator<Integer> iterator = Stream.iterate(0, u -> u += 1).limit(10).iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }

        Object[] objects = Stream.iterate(0, u -> u += 1).limit(10).toArray();
        System.out.println("Object Array: " + objects);

        try {
            Integer integer = (Integer) objects[0];
            System.out.println("number: " + integer);

            //System.out.println("下面代码抛出异常");
            //Integer[] integer = (Integer[]) objects; // Throws exception
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        Integer[] integers = Stream.iterate(0, u -> u += 1).limit(10).toArray(Integer[]::new);
        System.out.println("Integer Array: " + integers);

        Set<String> collect = noVowels().collect(Collectors.toSet());
        show("noVowelSet: ", collect);

        String collect1 = noVowels().collect(Collectors.joining("-->"));
        System.out.println("joining: " + collect1);

        Integer collect2 = noVowels().mapToInt(String::length).sum();
        System.out.println(collect2);
    }
}
