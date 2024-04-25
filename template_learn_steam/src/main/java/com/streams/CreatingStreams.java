package com.streams;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class CreatingStreams {
    public static<T> void show(String title, Stream<T> stream) {
        final int SIZE = 10;
        //skip与limit相反，跳过
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
        /**
         * 基本创建流操作
         * Stream.of() / Stream.empty()
         * Stream.generate() / Stream.iterate()
         * Pattern.compile("正则表达式").splitAsStream()
         * Files.lines
         * StreamSupport.stream
         */
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

        // Pattern类来创建一个字符串流 splitAsStream(CharSequence input) 产生一个流 他的元素是利用compile中的正则处理输入文本contents后的元素
        // 这种方法非常适合用于文本处理，尤其是在需要将一大段文本拆分成单独的词汇时。它提供了一种流式处理文本的方式，可以进一步使用Java Stream API进行如过滤、映射、统计等操作。
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


        /**
         * filter map flatMap
         */
        // filter 产生一个流，他包含当前流中所有满足谓词条件的元素
        Stream<Double> filter = Stream.iterate(1.0, t -> t * 2).filter(p -> p % 4 == 0).limit(3);
        show("filter", filter);

        // map(function mapper) 产生一个流，它包含将mapper应用在所有元素产生的结果，换言之，元素都要经过一遍运行
        Stream<String> map = Stream.of("A", "B", "C").map(m -> m.toLowerCase());
        show("map", map);

        // flatMap 产生一个流，他是将所有元素运行后产生的结果连接到一起而获得的（注意：每个结果都是一个流）
        // 用map操作，如下"hello" 和 "👋" 会被转换成stream对象，也就是说流中的元素，经过方法后也变成了流，就像Stream<Stream<String>> [["H", "e", "l", "l", "o"],["👋"]]
        // 用flatMap就会平坦每个流中的流，Stream<String>，也就是[["H", "e", "l", "l", "o"],["👋"]] ---> ["H", "e", "l", "l", "o","👋"]
        String s = "hello \uD83D\uDC4B";
        Stream<Stream<String>> streamStream = Stream.of(s.split(" ")).map(m -> codePoints(m));
        Stream<String> flatMap = Stream.of(s.split(" ")).flatMap(m -> codePoints(m));
        show("flatMap", flatMap);


        /**
         * 抽取子流和组合流
         * limit / skip
         * takeWhile / dropWhile
         * concat
         */
        // takeWhile 产生一个流，用于从流中选择元素，直到给定的谓词第一次返回false。
        // 这意味着它会从流的开头开始处理元素，并在遇到第一个不满足条件的元素时停止处理，返回之前所有满足条件的元素组成的新流。
        //List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        //List<Integer> result = numbers.stream()
        //        .takeWhile(n -> n < 4)
        //        .collect(Collectors.toList());// 结果：[1, 2, 3]

        // dropWhile 与takeWhile相反，它从流的开始处理元素，并且丢弃满足谓词的元素，直到谓词第一次返回false。
        // 一旦谓词对某个元素返回false，dropWhile就会停止丢弃元素，并包括该元素及其后的所有元素在内。
        //Stream<String> filterNumber = numberStream.filter(s -> Integer.valueOf(s) > 5);
        //List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        //List<Integer> result = numbers.stream()
        //        .dropWhile(n -> n < 4)
        //        .collect(Collectors.toList());// 结果：[4, 5]

        // concat 产生一个流 将两个流来连结起来
        Stream<String> concat = Stream.concat(codePoints("Hello"), codePoints("World"));
        show("concat", concat);


        /**
         * 其他的流转换
         */
        // distinct 将流中元素进行去重
        Stream<String> distinct = Stream.of("111", "222", "111", "111").distinct();
        show("distinct",distinct);

        // sorted: 产生一个流，他的元素是原有流元素按照一定顺序排列的元素
        // Comparator.comparing(String::length) 使用字符串的长度作为排序的依据。
        // String::length 是一个方法引用，指向 String 类的 length() 方法，该方法返回字符串的长度
        // reversed(): 返回一个新的比较器，该比较器将原来的比较结果倒转
        Stream<String> common = Stream.of("1", "22", "333", "4444");
        Stream<String> sorted = common.sorted(Comparator.comparing(String::length).reversed());
        show("sorted", sorted);

        //peek :产生一个元素相同的流，但是每次访问一个元素时，都会调用一个函数，用来调试很方便；
        //以下代码验证了iterate生成流时被惰性处理的，因为并没有打印，但是当被show方法运行的时候，打印了
        Stream<Double> peek = Stream.iterate(1.0, p -> p * 2).peek(e -> System.out.println("Fetching" + e)).limit(20);
        show("peek", peek);


        /**
         * 约简操作，返回值是Optional
         * count() max() min()
         * findFirst()  findAny()
         * anyMatch() allMatch()  noneMatch()
         */
        // max:获取流中最大值  compareToIgnoreCase方法为按照字典排序  min:获取最小值
        Optional<String> max = Stream.of("AAA","BBB","CCC").max(String::compareToIgnoreCase);
        System.out.println("max: " + max.orElse(""));

        // findFirst:返回非空集合中的第一个值  通常与filter组合使用更有效果 如下面的找到第一个以字母Q为首字母的单词，前提是存在这样的单词
        Optional<String> first = Stream.of("1", "Q", "QQ").filter(p -> p.startsWith("Q")).findFirst();
        System.out.println("first: " + first.orElse(""));

        // findAny:返回非空集合中的任意一个满足的值。 在并行流中使用 findAny() 时，它通常更快地返回结果，因为它只需找到满足流操作条件的任何一个元素即可
        // parallel(): 并行流
        // orElse(): 在Java的Optional类中，orElse方法是一种提供默认值的方式，用于处理可能为空的情况,它提供了一种更优雅的方式来处理null值，避免直接使用null导致的NullPointerException。
        Optional<String> any = Stream.of("1", "Q", "QA").parallel().filter(p -> p.startsWith("Q")).findAny();
        System.out.println("any:" + any.orElse(""));

        //anyMatch: 判断断言引元是否和流元素存在匹配    allMatch：判断断言引元是否和所有流元素存在匹配  noneMatch：判断断言引元是否和所有流元素不匹配
        //三者均通过并行获益
        boolean anyMatch = Stream.of("1", "Q", "QA").parallel().anyMatch(p -> p.startsWith("Q"));
        System.out.println("anyMatch: " + anyMatch);
        boolean allMatch = Stream.of("Q", "Q1", "QA").parallel().allMatch(p -> p.startsWith("Q"));
        System.out.println("allMatch: " + allMatch);
        boolean noneMatch = Stream.of("1", "2", "A").parallel().noneMatch(p -> p.startsWith("Q"));
        System.out.println("noneMatch: " + noneMatch);
    }

    /**
     * 字符串转换成流
     * 对于字符串 "Hello 👋"，该方法将分别输出 "H", "e", "l", "l", "o", " ", 和 "👋"
     */
    public static Stream<String> codePoints(String s){
        //存储字符串中的每一个Unicode码点
        ArrayList<String> result = new ArrayList<>();
        int i = 0;
        //将字符串拆分为其组成的各个Unicode字符，这对于处理多字节字符集（如表情符号或其他非拉丁字符）尤其有用。
        while (i < s.length()){
            //计算从位置 i 开始的第一个码点的结束位置。这是必要的因为某些Unicode字符可能由多个Java char 值组成（如代理对）
            int j = s.offsetByCodePoints(i, 1);
            //提取字符串，这段子字符串代表一个完整的Unicode码点
            result.add(s.substring(i,j));
            //更新索引
            i = j;
        }
        return result.stream();
    }
}
