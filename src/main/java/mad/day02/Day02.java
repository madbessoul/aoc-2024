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
        List<String> lines = FileUtils.readLines(filename);

        int nbSafeReports = 0;
        for (String line : lines) {

            int[] numbers = Arrays.stream(line.split(" ")).mapToInt(Integer::parseInt).toArray();
            if (isSafeAfterDampening(numbers)) {
                nbSafeReports++;
            }
        }
        return nbSafeReports + "";
    }

    private boolean isSafeAfterDampening(int[] numbers) {

        // First check
        int[] firstFailureIndices = checkForFailure(numbers);

        // Safe in 1 pass
        if (firstFailureIndices.length == 0) {
            return true;
        }

        // Bloody hell should've used ArrayList, this looks like Go code
        int[] dampenedNumbers = new int[numbers.length - 1];

        // We only need to try removing values around the first failure, so 3 tries at most.
        // It's still brute force but it worked on my machine :)
        for (int indexToRemove : firstFailureIndices) {

            // Create the dampened array using a loop
            for (int i = 0, j = 0; i < numbers.length; i++) {
                if (i != indexToRemove && indexToRemove >= 0 && indexToRemove < numbers.length) {
                    dampenedNumbers[j++] = numbers[i];
                }
            }
            // Check for safety and return immediately if it is
            if (isStrictlyMonotonicAndValid(dampenedNumbers)) {
                return true;
            }
        }
        return false;
    }

    private int[] checkForFailure(int[] numberList ) {

        int n = numberList.length;
        boolean increasing = true;
        boolean decreasing = true;

        for (int i = 0; i < n - 1 ; i++) {

            // I'll keep this here if I ever figure out how to filter the indices :/
            int[] failureIndices = new int[]{i-1, i, i+1};

            if (Math.abs(numberList[i] - numberList[i + 1]) > 3) {
                System.out.println("Too big jump at " + i);
                return failureIndices;
            }

            if (numberList[i] == numberList[i + 1]) {
                System.out.println("Stable at " + i);
                return failureIndices;
            }

            String lastChange = "";
            if (numberList[i] > numberList[i + 1]) {
                lastChange = "decreasing";
                increasing = false;
            } else if (numberList[i] < numberList[i + 1]) {
                lastChange = "increasing";
                decreasing = false;
            }

            if (!increasing && !decreasing) {
                System.out.println(lastChange + " at " + i);
                return failureIndices;
            }
        }

        // if success, return empty array
        return new int[]{};
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
