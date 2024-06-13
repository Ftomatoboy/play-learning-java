package com.streams;

import java.util.*;
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
        /**
         * Collectors.counting() 用于统计流中元素的个数
         */
        long counting = list.stream().collect(Collectors.counting());
        System.out.println("counting: "+counting);
        /**
         * Collectors.joining() 方法用某个指定的拼接字符串把所有元素拼接成一个字符串，并添加可选的前缀和后缀
         */
        List<String> alist = Arrays.asList("A","B","C","D");
        String joining=  alist.stream().collect(Collectors.joining(",","(",")"));
        System.out.println("joining: "+joining);
        /**
         * max min
         * 使用 Comparator.naturalOrder() 简化比较器的定义。
         * 直接使用 System.out::println 作为 ifPresent 的方法引用，简化代码
         */
        //Get Max
        list.stream().max(Comparator.naturalOrder()).ifPresent(System.out::println);
        //Get Min
        list.stream().min(Comparator.naturalOrder()).ifPresent(System.out::println);
        /**
         * Collectors.summingInt() 方法将流中的所有元素视为 int 类型，并计算所有元素的总和 ( sum )
         * Collectors.summingLong()
         * Collectors.summingDouble()
         */
        int summingInt = list.stream().collect(Collectors.summingInt(i -> i));
        System.out.println("summingInt: "+summingInt);
        /**
         * Collectors.toList() 将流中的所有元素导出到一个列表 ( List ) 中
         * Collectors.toSet()
         * Collectors.toMap()
         */
        List<Integer> collect2 = list.stream().collect(Collectors.toList());
        collect2.forEach(s->System.out.println("toList"+s));
        /**
         * Collectors.mapping() 一般用于多重 map and reduce 中。
         * 1.根据 Person 对象的 age 属性对流进行分组。
         * 2.对每个年龄组，提取 name 属性，并用逗号连接这些名字。
         */
        List<Person> personList = Person.getList();
        Map<Integer, String> nameByAge
                = personList.stream().collect(Collectors.groupingBy(Person::getAge, Collectors.mapping(Person::getName, Collectors.joining(","))));
        nameByAge.forEach((k,v)->System.out.println("Age:"+k +"  Persons: "+v));
    }
}
class Person {
    private String name;
    private int age;
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
    public String getName() {
        return name;
    }
    public int getAge() {
        return age;
    }
    public static List<Person> getList() {
        List<Person> list = new ArrayList<>();
        list.add(new Person("Ram", 30));
        list.add(new Person("Shyam", 20));
        list.add(new Person("Shiv", 20));
        list.add(new Person("Mahesh", 30));
        return list;
    }
}
