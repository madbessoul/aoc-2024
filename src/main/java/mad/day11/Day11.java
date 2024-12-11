package mad.day11;

import mad.Day;
import mad.common.FileUtils;

import java.util.*;
import java.util.stream.IntStream;

public class Day11 implements Day {
    private static long calculateFinalSize(List<Long> initialStones, int repeats) {

        // Frequency map
        // It SEEMS like there are many duplicates in the successive arrays #hopeium
        Map<Long, Long> numberFreq = new HashMap<>();

        // Initialize frequency map
        for (Long stone : initialStones) {
            numberFreq.merge(stone, 1L, Long::sum);
        }

        for (int i = 0; i < repeats; i++) {
            Map<Long, Long> nextFreq = new HashMap<>();

            for (Map.Entry<Long, Long> entry : numberFreq.entrySet()) {
                Long num = entry.getKey();
                Long freq = entry.getValue();

                if (num == 0) {
                    nextFreq.merge(1L, freq, Long::sum);
                } else if (String.valueOf(num).length() % 2 == 0) {

                    // Aargh I hate this parse and valueOf bullshit
                    String numStr = String.valueOf(num);
                    int mid = numStr.length() / 2;
                    long left = Long.parseLong(numStr.substring(0, mid));
                    long right = Long.parseLong(numStr.substring(mid).replaceFirst("^0+(?!$)", ""));
                    nextFreq.merge(left, freq, Long::sum);
                    nextFreq.merge(right, freq, Long::sum);
                } else {
                    nextFreq.merge(num * 2024, freq, Long::sum);
                }
            }

            numberFreq = nextFreq;
        }

        // Calculate final size
        return numberFreq.values().stream().mapToLong(Long::longValue).sum();
    }

    @Override
    public String part1(String filename) {
        List<Long> lines = FileUtils.readLines(filename).stream()
                .flatMap(s -> Arrays.stream(s.split(" ")))
                .map(Long::parseLong)
                .toList();

        long size = calculateFinalSize(lines, 25);
        return String.valueOf(size);
    }

    @Override
    public String part2(String filename) {
        List<Long> lines = FileUtils.readLines(filename).stream()
                .flatMap(s -> Arrays.stream(s.split(" ")))
                .map(Long::parseLong)
                .toList();

        long size = calculateFinalSize(lines, 75);
        return String.valueOf(size);
    }
}
