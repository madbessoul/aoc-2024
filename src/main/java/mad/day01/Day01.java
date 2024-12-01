package mad.day01;

import mad.Day;
import mad.common.FileUtils;

import java.util.List;
import java.util.stream.IntStream;

public class Day01 implements Day {

    @Override
    public String part1(String fileName) {
        List<String> lines = FileUtils.readLines(fileName);
        var left = lines.stream().map(item -> item.split("   ")[0]).sorted().map(Integer::parseInt).toList();
        var right = lines.stream().map(item -> item.split("   ")[1]).sorted().map(Integer::parseInt).toList();
        var result = IntStream.range(0, left.size()).map(i -> Math.abs(left.get(i) - right.get(i))).sum();
        return result + "";
    }

    @Override
    public String part2(String fileName) {
        List<String> lines = FileUtils.readLines(fileName);
        var left = lines.stream().map(item -> item.split("   ")[0]).sorted().map(Integer::parseInt).toList();
        var right = lines.stream().map(item -> item.split("   ")[1]).sorted().map(Integer::parseInt).toList();
        var result = left.stream().mapToLong(integer -> {
            long leftValue = integer;
            long countValue = right.stream().filter(item -> item == leftValue).count();
            return leftValue * countValue;
        }).sum();
        return result + "";
    }
}
