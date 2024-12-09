package mad.day09;

import mad.Day;
import mad.common.FileUtils;

import java.util.*;

public class Day09 implements Day {
    private List<String> stringToArray(String s) {
        return  s.chars().mapToObj(c -> String.valueOf((char) c)).toList();
    }

    @Override
    public String part1(String filename) {
        List<String> lines = FileUtils.readLines(filename);
        List<String> mergedLines = lines.stream().flatMap(line -> Arrays.stream(line.split("\n"))).toList();
        List<String> expandedData = mergedLines.getFirst().chars().mapToObj(c -> String.valueOf((char) c)).toList();

        List<String> diskMap = new ArrayList<>();

        int fileIndex = 0;
        for (int i = 0; i < expandedData.size(); i++) {
            if (i % 2 == 0) {
                diskMap.addAll(stringToArray(String.valueOf(fileIndex).repeat(Integer.parseInt(expandedData.get(i)))));
                fileIndex++;
            } else {
                diskMap.addAll(stringToArray(".".repeat(Integer.parseInt(expandedData.get(i)))));
            }
        }
        return "";
    }

    @Override
    public String part2(String filename) {
        return "";
    }
}
