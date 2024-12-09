package mad.day03;

import mad.Day;
import mad.common.FileUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day03 implements Day {
    @Override
    public String part1(String filename) {
        List<String> lines = FileUtils.readLines(filename);
        Pattern pattern = Pattern.compile("mul\\((\\d+),(\\d+)\\)");
        AtomicReference<Integer> result = new AtomicReference<>(0);
        lines.forEach(line -> {
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                int a = Integer.parseInt(matcher.group(1));
                int b = Integer.parseInt(matcher.group(2));
                result.updateAndGet(v -> v + a * b);
            }
        });
        return result.get() + "";
    }

    public String part2(String filename) {
        String lines = FileUtils.readLines(filename).stream().collect(Collectors.joining(""));
        Pattern regex = Pattern.compile("mul\\((\\d+),(\\d+)\\)|don't\\(\\)|do\\(\\)");
        Matcher matcher = regex.matcher(lines);

        int sum = 0;
        boolean flag = true;
        while (matcher.find()) {
            if ("do()".equals(matcher.group())) {
                flag = true;
            } else if ("don't()".equals(matcher.group())) {
                flag = false;
            } else {
                if (flag) {
                    int a = Integer.parseInt(matcher.group(1));
                    int b = Integer.parseInt(matcher.group(2));
                    sum += a * b;
                }
            }
        }
        return sum + "";
    }
}
