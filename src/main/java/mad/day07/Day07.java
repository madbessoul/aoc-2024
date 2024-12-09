package mad.day07;

import mad.Day;
import mad.common.FileUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class Day07 implements Day {


    private Long parseResult(String line) {
        return Long.parseLong(line.split(":")[0]);
    }

    private List<Integer> parseNumbers(String line) {
        String rightSide = line.split(":")[1];
        return Stream.of(rightSide.split(" ")).filter(v -> !v.isEmpty()).map(Integer::parseInt).toList();
    }

    private Long computeOperation(Long left, int right, char operator) {
        switch (operator) {
            case '0' -> {
                return left + right;
            }
            case '1' -> {
                return left * right;
            }
            case '2' -> {
                // Concatenation
                return Long.valueOf(left.toString() + right);
            }
            default -> throw new RuntimeException("Invalid operator: " + operator);
        }
    }

    private String decToBaseX(int number, int maxBits, int radix) {
        switch (radix) {
            case 2:
                // I used bitshifting, I can now die in peace
                return Integer.toBinaryString((1 << maxBits) | number).substring(1);
            case 3: {
                // Dunno if there is a better way to do this
                String b = Integer.toString(number, 3);
                if (b.length() < maxBits) {
                    b = String.join("", Collections.nCopies(maxBits - b.length(), "0")) + b;
                }
                return b;
            }
            default:
                throw new RuntimeException("Unsupported base: " + radix);
        }
    }

    private void displayEquation(List<Integer> numbers, String operators) {
        String resultString = "";
        for (int i = 0; i < numbers.size(); i++) {
            resultString += numbers.get(i);
            if (i < numbers.size() - 1) {
                String op = switch (operators.charAt(i)) {
                    case '0' -> "+";
                    case '1' -> "*";
                    case '2' -> "||";
                    default -> throw new RuntimeException("Invalid operator: " + operators.charAt(i));
                };
                resultString += " " + op + " ";
            }
        }
        System.out.println(resultString);
    }
    private boolean validateEquation(Long result, List<Integer> numbers, int numOperators) {

        // Look... (+) is 0, (*) is 1 yeah ?
        // We look for all combinations so basically iterate over binary numbers
        // With len - 1 bits. Could work.

        // Bahahaha, base 3 for part 2 now. (||) is 2
        for (int i = 0; i < Math.pow(numOperators, numbers.size() - 1); i++) {

            Long left = Long.valueOf(numbers.getFirst());
            String operators = decToBaseX(i, numbers.size() - 1, numOperators);
            // displayEquation(numbers, operators);


            // Coalesce the operation result into the left side and keep going left
            for (int j = 1; j < numbers.size(); j++) {

                // Trying to make it faster... maybe
                if (left > result) {
                    continue;
                }

                left = computeOperation(left, numbers.get(j), operators.charAt(j - 1));
            }

            if (Objects.equals(left, result)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String part1(String filename) {
        List<String> lines = FileUtils.readLines(filename);
        Long sum = lines.stream()
                .filter(v -> validateEquation(parseResult(v), parseNumbers(v), 2))
                .map(this::parseResult)
                .reduce(0L, Long::sum);

        return String.valueOf(sum);
    }

    @Override
    public String part2(String filename) {
        List<String> lines = FileUtils.readLines(filename);
        Long sum = lines.stream()
                .filter(v -> validateEquation(parseResult(v), parseNumbers(v), 3))
                .map(this::parseResult)
                .reduce(0L, Long::sum);

        return String.valueOf(sum);
    }
}
