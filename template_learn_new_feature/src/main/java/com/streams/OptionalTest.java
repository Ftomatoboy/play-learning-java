package com.streams;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class OptionalTest {
    public static void main(String[] args) {
        List<String> wordList = new ArrayList<>();
        wordList.add("A");
        wordList.add("B");
        wordList.add("C");
        wordList.add("D");
        wordList.add("E");
        wordList.add("F");

        /**
         * 基本Optional操作
         * Optional.empty()   Optional.of  Optional.ofNullable
         * orElse  orElseGet  orElseThrow/get
         * ifPresent()  ifPresentOrElse()
         */
        // 除了通过stream转换成Optional类型，他本身也可以自行创建
        Optional<String> emptyOptional = Optional.empty(); // 空Optional
        Optional<List<String>> notEmptyOptional = Optional.of(wordList); // 创建Optional，若notEmptyOptional为null,则抛出空指针异常
        Optional<List<String>> a = Optional.ofNullable(wordList);// 创建Optional，若notEmptyOptional为null,则创建空Optional（Optional.empty()）

        // orElse(T other) 获取Optional的值，如果是null，变为other
        String orElse = emptyOptional.orElse("empty");
        System.out.println("orElse:" + orElse);

        // 获取Optional的值，如果 emptyOptional 为空，那么会执行 () -> System.getProperty("myapp.default") 这个 Lambda 表达式，获取系统属性 "myapp.default" 的值作为备用值，并将其返回给变量 o
        Object o = emptyOptional.orElseGet(() -> System.getProperty("myapp.default"));
        System.out.println("orElseGet:" + o);

        // 获取Optional的值，如果emptyOptional 为空，抛出异常IllegalAccessException,等同于get()
        try {
            emptyOptional.orElseThrow(IllegalAccessException::new);
            emptyOptional.get();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        // ifPresent() 如果Optional值存在，则他会被传递给该函数
        HashSet<String> results = new HashSet<>();
        Optional<String> optional = Stream.of("optional1", "optional2").findAny();
        boolean present = optional.isPresent(); // 判断optional是否有值
        optional.ifPresent(results :: add); // 若optional存在，则给results新增操作
        System.out.println("ifPresent:" + results);

        // 如果optional有值，则执行第一个操作；否则执行第二个操作 java9
        // optional.ifPresentOrElse(
        //        value -> System.out.println("Value is: " + value),
        //        () -> System.out.println("Value is absent")
        //);

        /**
         * filter map flatMap or
         */
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("hello world");
        arrayList.add("new world");

        // filter 筛选条件成立的元素组成新的Optional
        Optional<ArrayList<String>> hello = Optional.of(arrayList).filter(v -> v.contains("hello"));
        System.out.println("filter:" + hello.orElse(new ArrayList<>()));

        // map: 对Optional中的值应用一个函数，并返回一个新的Optional对象，该对象包含原始Optional值经过函数处理后的结果
        Optional<Boolean> map1 = Optional.of("new world2").map(arrayList::add); // 返回操作是否成功
        String map2 = Optional.of("new world3").map(n -> n.toUpperCase()).orElse(""); // 对自身的Optional对象操作会返回新的对象
        System.out.println("map1:" + map1.orElse(false));
        System.out.println("map2:" + map2);

        // flatMap:处理链式Optional操作中一个非常有用的工具，尤其在你需要进行多个连续的可选操作，每个操作都可能返回Optional对象时。它帮助保持代码的简洁性，并且避免了不必要的嵌套和复杂的空值检查。
        System.out.println(inverse(4.0).flatMap(OptionalTest::squareRoot));
        System.out.println(inverse(-1.0).flatMap(OptionalTest::squareRoot));
        System.out.println(inverse(0.0).flatMap(OptionalTest::squareRoot));
        System.out.println(Optional.of(-4.0).flatMap(OptionalTest::inverse).flatMap(OptionalTest::squareRoot));

        //or:若optional值存在，则返回optional对象，否则计算lambda表达式返回,常用于Optional二选一   java9
        // 如使用or方法获取电子邮件，优先获取主要电子邮件，如果没有，则尝试获取次要电子邮件
        // Optional<Double> number = inverse(0.0).or(() -> squareRoot(2.0));
    }

    public static Optional<Double> inverse(Double x){
        return x == 0 ? Optional.empty() : Optional.of(1 / x);
    }

    public static Optional<Double> squareRoot(Double x){
        return x < 0 ? Optional.empty() : Optional.of(Math.sqrt(x));
    }
}
