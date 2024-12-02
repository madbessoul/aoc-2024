package mad.day02;

import mad.Day;
import mad.common.FileUtils;

import java.util.Arrays;
import java.util.List;

public class Day02 implements Day {
    @Override
    public String part1(String filename) {
        List<String> lines = FileUtils.readLines(filename);

        int nbSafeReports = 0;
        for (String line : lines) {
            int[] numbers = Arrays.stream(line.split(" ")).mapToInt(Integer::parseInt).toArray();
            if (isStrictlyMonotonicAndValid(numbers)) {
                nbSafeReports++;
            }
        }
        return nbSafeReports + "";

    }

    @Override
    public String part2(String filename) {
        return "";
    }

    private boolean isStrictlyMonotonicAndValid(int[] numberList ) {

        int n = numberList.length;
        boolean increasing = true;
        boolean decreasing = true;

        for (int i = 0; i < n - 1 ; i++) {

            if (Math.abs(numberList[i] - numberList[i + 1]) > 3) {
                return false;
            }

            if (numberList[i] == numberList[i + 1]) {
                 return false;
            }

            if (numberList[i] >= numberList[i + 1]) {
                increasing = false;
            } else if (numberList[i] <= numberList[i + 1]) {
                decreasing = false;
            }

            if (!increasing && !decreasing) {
                return false;
            }
        }
        return true;
    }
}
