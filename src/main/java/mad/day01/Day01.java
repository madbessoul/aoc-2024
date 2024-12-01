package mad.day01;

import mad.Day;
import mad.common.FileUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day01 implements Day {

    @Override
    public String part1(String fileName) {
        List<String> lines = FileUtils.readLines(fileName);
        var left = lines.stream().map(item -> item.split(" +")[0]).sorted().map(Integer::parseInt).toList();
        var right = lines.stream().map(item -> item.split(" +")[1]).sorted().map(Integer::parseInt).toList();
        var result = IntStream.range(0, left.size()).map(i -> Math.abs(left.get(i) - right.get(i))).sum();
        return result + "";
    }

    @Override
    public String part2(String fileName) {

        var parseStart = Instant.now();
        List<String> lines = FileUtils.readLines(fileName);
        var parseEnd = Instant.now();
        System.out.println("Read time: " + Duration.between(parseStart, parseEnd).toMillis() + " ms");

        var algStart = Instant.now();

        Map<String, Long> leftOccurences = lines.stream()
                .map(item -> item.split(" +")[0])
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        Map<String, Long> rightOccurences = lines.stream()
                .map(item -> item.split(" +")[1])
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        AtomicLong result = new AtomicLong(0L);
        leftOccurences.forEach((key, value) -> {
            result.addAndGet(Long.parseLong(key) * value * rightOccurences.getOrDefault(key, 0L));
        });

        var algEnd = Instant.now();
        System.out.println("Alg time: " + Duration.between(algStart, algEnd).toMillis() + " ms");
        return result + "";
    }
}
