package com.streams;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class CreatingStreams {
    public static<T> void show(String title, Stream<T> stream) {
        final int SIZE = 10;
        List<T> firstElements = stream.limit(SIZE + 1).collect(Collectors.toList()); //取前11个
        System.out.println(title + ": ");
        for (int i = 0; i < firstElements.size(); i++){
            if (i > 0) System.out.println(", ");
            if (i < SIZE) System.out.println(firstElements.get(i));
            else System.out.println("...");
        }
        System.out.println();
    }

    public static void main(String[] args) throws IOException {
        System.out.println(System.getProperty("user.dir")); // 判断当前工作目录
        Path path = Paths.get("../gutenberg/alice30.txt");
        String contents = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);

        // Stream.of 产生一个给定值的流,其中参数可以是"T..."  Stream.empty 产生一个不包含任何元素的流
        Stream<String> words = Stream.of(contents.split("\\PL+"));
        show("words",words);
        Stream<String> song = Stream.of("gently", "down", "the", "stream");
        show("song",song);
        Stream<Object> empty = Stream.empty();
        show("empty", empty);
        String idStr = "2003,1757,1753,1744,1691,1687,1550,840,811,793,790,680,564,537,522,398,354,314,9,17,21,42,53,54,711,448,306";
        List<String> collect = Stream.of(idStr.split(",")).collect(Collectors.toList());
        for (String id : collect){
            System.out.println("collect" + id);
        }

        // Stream.generate(Supplier<T> s)产生一个无限流，他的值是通过反复调用函数s来构建的
        Stream<String> echos = Stream.generate(() -> "Echo");
        show("echos", echos);
        Stream<Double> randoms = Stream.generate(Math::random);
        show("randoms", randoms);

        //Stream.iterate(T seed, UnaryOperator f) 产生一个无限流，它的值包括seed，以及在seed上调用f产生的值，以及在前一个元素调用f产生的值...
        Stream<BigInteger> integer = Stream.iterate(BigInteger.ONE, n -> n.add(BigInteger.ONE));
        show("integer", integer);

        // splitAsStream(CharSequence input) 产生一个流 他的元素是输入中该模式界定的部分
        Stream<String> wordsAnotherWay = Pattern.compile("\\PL+").splitAsStream(contents);
        show("wordsAnotherWay", wordsAnotherWay);

        // lines(Path p, Charset cs) 返回一个流，他的元素是文件中的行
        try (Stream<String> lines = Files.lines(path, StandardCharsets.UTF_8)){ // 关闭文件流
            show("lines", lines);
        }

        /*
        * FileSystems.getDefault()：这个方法返回默认的 FileSystem 实例，它提供对当前文件系统的访问。在大多数环境中，这将是操作系统的文件系统。
        * getRootDirectories()：此方法返回一个 Iterable<Path>，表示文件系统中所有可用的根目录。在 Windows 上，这可能是所有驱动器的根（如 C:, D:\ 等），而在类 Unix 系统上，通常只有一个根目录 "/"。
        * pathIterable.spliterator()：这个方法从 Iterable 中创建一个 Spliterator(可分割的迭代器)。Spliterators 是 Java 8 引入的，用于提供更高效的并行迭代操作的能力
        * StreamSupport.stream()：这个静态方法从一个 Spliterator 创建一个 Stream。这里的第二个参数 false 表示创建的流是非并行的，即它是一个顺序流
        */
        Iterable<Path> pathIterable = FileSystems.getDefault().getRootDirectories(); // 根目录的值
        Stream<Path> rootDirectories = StreamSupport.stream(pathIterable.spliterator(), false); // 元素为根目录的值(迭代器产生的值)
        show("rootDirectories", rootDirectories);
    }
}
