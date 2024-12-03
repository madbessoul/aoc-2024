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

    @Override
    public String part2(String filename) {
        List<String> lines = FileUtils.readLines(filename);
        Pattern exclusionPattern = Pattern.compile("(?<=don't\\(\\))(.*?)(?=do\\(\\))", Pattern.DOTALL | Pattern.MULTILINE);
        AtomicInteger result = new AtomicInteger();
        String fulldata = lines.stream().collect(Collectors.joining(""));
        System.out.println(fulldata);
        Matcher exclusionMatcher = exclusionPattern.matcher(fulldata);
        List<Integer> breakpoints = new ArrayList<>(){};

        // First breakpoint is always at the beginning of the file
        breakpoints.add(0);
        while (exclusionMatcher.find()) {
            System.out.println(exclusionMatcher.group(1));
            breakpoints.add(exclusionMatcher.start(1));
            breakpoints.add(exclusionMatcher.end(1));
        }
        // Houston, we have a problem!
        // We can have a don't() between the last breakpoint and the end of the file
        String lastStringChunk = fulldata.substring(breakpoints.getLast());
        int lastBreakpoint = breakpoints.get(breakpoints.size() - 1) + lastStringChunk.indexOf("don't()");
        breakpoints.add(lastBreakpoint);
        System.out.println(breakpoints);
        System.out.println(breakpoints.size());

        // Loop across the enabled ranges
        for (int i = 0; i < breakpoints.size(); i += 2) {
            System.out.println(breakpoints.get(i) + ":" + breakpoints.get(i + 1));
            String cleanLine = fulldata.substring(breakpoints.get(i), breakpoints.get(i + 1));
            result.addAndGet(matchMultiplyAndAdd(cleanLine));
        }

        return result + "";
    }

    private static int matchMultiplyAndAdd(String line) {
        int result = 0;
        Pattern pattern = Pattern.compile("mul\\((\\d+),(\\d+)\\)");
        Matcher matcher = pattern.matcher(line);
        System.out.println(line);
        while (matcher.find()) {
            System.out.println(matcher.group(1) + " " + matcher.group(2));
            int a = Integer.parseInt(matcher.group(1));
            int b = Integer.parseInt(matcher.group(2));
            result += a * b;
        }
        return result;
    }
}
