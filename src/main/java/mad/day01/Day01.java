package mad.day01;

import mad.Day;
import mad.common.FileUtils;

import java.util.Arrays;
import java.util.List;

public class Day01 implements Day {

    @Override
    public String part1(String fileName) {
        int[][] grid = FileUtils.readGrid(fileName);
        List<String> lines = FileUtils.readLines(fileName);
        System.out.println(Arrays.deepToString(grid));
        return "part 1 done";
    }

    @Override
    public String part2(String fileName) {
        List<String> lines = FileUtils.readLines(fileName);
        System.out.println(lines);
        return "part 2 done";
    }
}
