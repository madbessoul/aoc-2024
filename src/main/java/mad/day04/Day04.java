package mad.day04;

import mad.Day;
import mad.common.FileUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
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

    private List<Loc> getDirectionCoords(Loc loc, String direction) {
        return switch (direction) {
            case "N" -> List.of(new Loc(loc.x(), loc.y() + 1), new Loc(loc.x(), loc.y() + 2), new Loc(loc.x(), loc.y() + 3));
            case "S" -> List.of(new Loc(loc.x(), loc.y() - 1), new Loc(loc.x(), loc.y() - 2), new Loc(loc.x(), loc.y() - 3));
            case "E" -> List.of(new Loc(loc.x() + 1, loc.y()), new Loc(loc.x() + 2, loc.y()), new Loc(loc.x() + 3, loc.y()));
            case "W" -> List.of(new Loc(loc.x() - 1, loc.y()), new Loc(loc.x() - 2, loc.y()), new Loc(loc.x() - 3, loc.y()));
            case "NE" -> List.of(new Loc(loc.x() + 1, loc.y() + 1), new Loc(loc.x() + 2, loc.y() + 2), new Loc(loc.x() + 3, loc.y() + 3));
            case "SE" -> List.of(new Loc(loc.x() + 1, loc.y() - 1), new Loc(loc.x() + 2, loc.y() - 2), new Loc(loc.x() + 3, loc.y() - 3));
            case "SW" -> List.of(new Loc(loc.x() - 1, loc.y() - 1), new Loc(loc.x() - 2, loc.y() - 2), new Loc(loc.x() - 3, loc.y() - 3));
            case "NW" -> List.of(new Loc(loc.x() - 1, loc.y() + 1), new Loc(loc.x() - 2, loc.y() + 2), new Loc(loc.x() - 3, loc.y() + 3));
            default -> List.of();
        };
    }

    private String getWordInDirection(Map<Loc, Character> grid, Loc loc, String direction) {
        List<Loc> directionCoords = getDirectionCoords(loc, direction);
        String word =  directionCoords.stream()
                .map(grid::get)
                .filter(Objects::nonNull)
                .map(c -> Character.toString(c))
                .collect(Collectors.joining());
        return word;
    }

    private Long countXMAS(Map<Loc, Character> grid, Loc loc) {
        return Stream.of("N", "S", "E", "W", "NE","SE","SW","NW")
                .map(direction -> getWordInDirection(grid, loc, direction))
                .filter(word -> word.equals("MAS"))
                .count();
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
        return "";
    }
}
