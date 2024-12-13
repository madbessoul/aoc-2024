package mad.day13;

import mad.Day;
import mad.common.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class Day13 implements Day {

    private List<ClawMachine> parseMachines(List<String> lines) {
        List<ClawMachine> machines = new ArrayList<>();
        for (int i = 0; i < lines.size(); i = i + 4) {
            int a1 = Integer.parseInt(lines.get(i).split(":")[1].split(",")[0].substring(2));
            int b1 = Integer.parseInt(lines.get(i).split(":")[1].split(",")[1].substring(2));
            int a2 = Integer.parseInt(lines.get(i + 1).split(":")[1].split(",")[0].substring(2));
            int b2 = Integer.parseInt(lines.get(i + 1).split(":")[1].split(",")[1].substring(2));
            int c1 = Integer.parseInt(lines.get(i + 2).split(":")[1].split(",")[0].substring(3));
            int c2 = Integer.parseInt(lines.get(i + 2).split(":")[1].split(",")[1].substring(3));
            machines.add(new ClawMachine(a1, b1, a2, b2, c1, c2));
        }
        return machines;
    }

    @Override
    public String part1(String filename) {
        List<String> lines = FileUtils.readLines(filename);
        Long sum = parseMachines(lines).stream()
                .map(ClawMachine::calcFewestTokens)
                .filter(i -> i != 0)
                .mapToLong(i -> i)
                .reduce(0L, Long::sum);

        return String.valueOf(sum);
    }

    @Override
    public String part2(String filename) {
        List<String> lines = FileUtils.readLines(filename);
        Long sum = parseMachines(lines).stream()
                .map(ClawMachine::calcFewestTokensPart2)
                .filter(i -> i != 0)
                .reduce(0L, Long::sum);

        return String.valueOf(sum);
    }
}
