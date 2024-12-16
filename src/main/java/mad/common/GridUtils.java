package mad.common;

import mad.day04.Loc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GridUtils {
    public static Map<Loc, Character> gridToMap(List<String> lines) {
        Map<Loc, Character> map = new HashMap<>();
        for (int y = 0; y < lines.size(); y++) {
            for (int x = 0; x < lines.get(y).length(); x++) {
                map.put(new Loc(x, y), lines.get(y).charAt(x));
            }
        }
        return map;
    }

    private void printGrid(Map<Loc, Character> grid) {
        int maxX = grid.keySet().stream().mapToInt(Loc::x).max().orElse(0);
        int maxY = grid.keySet().stream().mapToInt(Loc::y).max().orElse(0);

        // ANSI color codes
        String RED = "\u001B[31m";
        String GREEN = "\u001B[32m";
        String ORANGE = "\u001B[33m";
        String GREY = "\u001B[37m";
        String RESET = "\u001B[0m";

        System.out.println("\n".repeat(50));

        // Print grid
        for (int y = 0; y <= maxY; y++) {
            for (int x = 0; x <= maxX; x++) {
                Character c = grid.getOrDefault(new Loc(x, y),  null);
                switch (c) {
                    case '@' -> System.out.print(GREEN + c + RESET);
                    case '#' -> System.out.print(RED + c + RESET);
                    case '.' -> System.out.print(GREY + c + RESET);
                    case '*' -> System.out.print(ORANGE + c + RESET);
                    default -> System.out.print(c);
                }
            }
            System.out.println();
        }
    }
}
