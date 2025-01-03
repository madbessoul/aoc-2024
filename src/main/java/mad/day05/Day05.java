package mad.day05;

import mad.Day;
import mad.common.FileUtils;

import java.util.Arrays;
import java.util.List;

public class Day05 implements Day {
    private RuleSet parseRules(String filename) {
        List<String> lines = FileUtils.readLines(filename);
        return new RuleSet(lines.stream()
                .filter(line -> line.matches("^\\d+\\|\\d+"))
                .map(stringRule -> {
                    String[] parts = stringRule.split("\\|");
                    return new Rule(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
                })
                .toList()
        );
    }

    private List<List<Integer>> parseUpdates(String filename) {
        List<String> lines = FileUtils.readLines(filename);
        return lines.reversed().stream()
                .takeWhile(line -> !"".equals(line))
                .map(line -> Arrays.asList(line.split(",")))
                .map(list -> list.stream().map(Integer::parseInt).toList())
                .toList();
    }

    @Override
    public String part1(String filename) {
        var ruleset = parseRules(filename);
        var updates = parseUpdates(filename);

        final Integer sum = updates.stream()
                .filter(ruleset::validateUpdate)
                .map(update -> update.get(update.size() / 2))
                .reduce(Integer::sum)
                .orElse(0);

        return String.valueOf(sum);
    }

    @Override
    public String part2(String filename) {
        var ruleset = parseRules(filename);
        var updates = parseUpdates(filename);

        final Integer sum = updates.stream()
                .filter(update -> !ruleset.validateUpdate(update))
                .map(update -> ruleset.subsequence(update).get(update.size() / 2))
                .reduce(Integer::sum)
                .orElse(0);

        return String.valueOf(sum);
    }
}
