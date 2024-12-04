package mad.day04;

import mad.Day;
import mad.common.FileUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day04 implements Day {

    private Map<Loc, Character> makeGridMap(List<String> lines) {
        Map<Loc, Character> grid = new HashMap<>();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            for (int j = 0; j < line.length(); j++) {
                grid.put(new Loc(i, j), line.charAt(j));
            }
        }
        return grid;
    }

    private List<Loc> getDirectionCoords(Loc loc, String direction, int distance) {
        return switch (direction) {
            case "N" -> IntStream.range(1, distance + 1).mapToObj(i -> new Loc(loc.x(), loc.y() + i)).toList();
            case "S" -> IntStream.range(1, distance + 1).mapToObj(i -> new Loc(loc.x(), loc.y() - i)).toList();
            case "E" -> IntStream.range(1, distance + 1).mapToObj(i -> new Loc(loc.x() + i, loc.y())).toList();
            case "W" -> IntStream.range(1, distance + 1).mapToObj(i -> new Loc(loc.x() - i, loc.y())).toList();
            case "NE" -> IntStream.range(1, distance + 1).mapToObj(i -> new Loc(loc.x() + i, loc.y() + i)).toList();
            case "SE" -> IntStream.range(1, distance + 1).mapToObj(i -> new Loc(loc.x() + i, loc.y() - i)).toList();
            case "SW" -> IntStream.range(1, distance + 1).mapToObj(i -> new Loc(loc.x() - i, loc.y() - i)).toList();
            case "NW" -> IntStream.range(1, distance + 1).mapToObj(i -> new Loc(loc.x() - i, loc.y() + i)).toList();
            default -> List.of();
        };
    }

    private String getWordInDirection(Map<Loc, Character> grid, Loc loc, String direction, int distance) {
        List<Loc> directionCoords = getDirectionCoords(loc, direction, distance);
        return directionCoords.stream()
                .map(grid::get)
                .filter(Objects::nonNull)
                .map(c -> Character.toString(c))
                .collect(Collectors.joining());
    }

    private Long countXMAS(Map<Loc, Character> grid, Loc loc) {
        return Stream.of("N", "S", "E", "W", "NE","SE","SW","NW")
                .map(direction -> getWordInDirection(grid, loc, direction, 3))
                .filter(word -> word.equals("MAS"))
                .count();
    }

    private boolean isAStar(Map<Loc, Character> grid, Loc loc) {
        List<String> allowedWords = List.of("SSMM", "MMSS","MSSM","SMMS");
        return Stream.of("NE","SE","SW","NW")
                .map(direction -> getWordInDirection(grid, loc, direction, 1))
                .reduce(String::concat)
                .map(allowedWords::contains)
                .orElse(false);
    }

    @Override
    public String part1(String filename) {
        List<String> lines = FileUtils.readLines(filename);
        Map<Loc, Character> grid = makeGridMap(lines);
        Long xmaxCount = grid.entrySet().stream()
                .filter(e -> e.getValue() == Character.valueOf('X'))
                .map(e -> countXMAS(grid, e.getKey()))
                .reduce(0L, Long::sum);

        return String.valueOf(xmaxCount);
    }

    @Override
    public String part2(String filename) {
        List<String> lines = FileUtils.readLines(filename);
        Map<Loc, Character> grid = makeGridMap(lines);
        Long starCount = grid.entrySet().stream()
                .filter(e -> e.getValue() == Character.valueOf('A'))
                .filter(e -> isAStar(grid, e.getKey()))
                .count();

        return String.valueOf(starCount);
    }


}
