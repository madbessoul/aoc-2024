package mad.day15;

import mad.Day;
import mad.common.FileUtils;
import mad.common.GridUtils;
import mad.day04.Loc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class Day15 implements Day {
    record GridState(Map<Loc, Character> grid, List<String> sequence, Loc currentPos) {

        Loc getNextPosition(int step, int length) {
            String direction = sequence.get(step);
            return switch (direction) {
                case "^" -> new Loc(currentPos.x(), currentPos.y() - length);
                case ">" -> new Loc(currentPos.x() + length, currentPos.y());
                case "v" -> new Loc(currentPos.x(), currentPos.y() + length);
                case "<" -> new Loc(currentPos.x() - length, currentPos.y());
                default -> throw new IllegalStateException("Unknown direction: " + direction);
            };
        }
        int numberOfBoxesAhead(int step) {

            Loc nextPos;
            int numberOfBoxes = 0;
            while (true) {
                nextPos = getNextPosition(step, numberOfBoxes + 1);
                if (grid.getOrDefault(nextPos, '#') == 'O') {
                    numberOfBoxes++;
                } else if (grid.getOrDefault(nextPos, '#') == '.') {
                    return numberOfBoxes;
                } else {
                    return -1;
                }
            }
        }

        GridState move(int step) {
            Map<Loc, Character> newGrid = new HashMap<>(grid);
            Loc nextPos = getNextPosition(step, 1);
            int numberOfBoxesAhead = numberOfBoxesAhead(step);

            int moveType = 0; // 0 = no move, 1 = free move, 2 = push box

            if (grid.getOrDefault(nextPos, '#') == '.') {
                moveType = 1;
            } else if (numberOfBoxesAhead > 0) {
                moveType = 2;
            }

            switch (moveType) {
                case 1 -> {
                    newGrid.remove(currentPos);
                    newGrid.put(currentPos, '.');
                    newGrid.put(nextPos, '@');
                    return new GridState(newGrid, sequence, nextPos);
                }
                case 2 -> {
                    // remove robot and and box in front
                    newGrid.remove(currentPos);
                    newGrid.remove(nextPos);

                    // Advance robot, put empty space in the back
                    // put a box at the end of the line
                    newGrid.put(currentPos, '.');
                    newGrid.put(nextPos, '@');
                    newGrid.put(getNextPosition(step, numberOfBoxesAhead + 1), 'O');
                    return new GridState(newGrid, sequence, nextPos);
                }
            }

            // If move isn't valid, stay in place
            return new GridState(grid, sequence, currentPos);
        }

        int getGridScore() {
            return grid.entrySet().stream()
                    .filter(e -> e.getValue().equals('O'))
                    .map(loc -> (loc.getKey().y() * 100) + loc.getKey().x())
                    .reduce(0, Integer::sum);
        }
    }

    private Loc findStart(Map<Loc, Character> grid) {
        return grid.entrySet().stream()
                .filter(e -> e.getValue().equals('@'))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No @ found in grid"));
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
                    case 'O' -> System.out.print(ORANGE + c + RESET);
                    default -> System.out.print(c);
                }
            }
            System.out.println();
        }
    }

    @Override
    public String part1(String filename) {
        List<String> lines = FileUtils.readLines(filename);
        var grid = GridUtils.gridToMap(lines.stream().takeWhile(l -> !l.isEmpty()).toList());
        var seqList = lines.reversed().stream()
                .takeWhile(l -> !l.isEmpty())
                .toList();
        var seq = seqList.reversed().stream()
                .flatMap(str -> str.chars().mapToObj(Character::toString))
                .toList();

        // Create initial state with @ position
        Loc startPos = findStart(grid);
        System.out.println(startPos);

        GridState state = new GridState(grid, seq, startPos);

        // Create scanner for input
        Scanner scanner = new Scanner(System.in);

        // Move once for each direction in sequence
        for (int i = 0; i < seq.size(); i++) {
            state = state.move(i);
            //printGrid(state.grid());
            if (i < seq.size() - 1) {
                // System.out.println("Step " + (i + 1) + ": " + state.sequence.get(i + 1));
            }
            //scanner.nextLine();
        }
        scanner.close();
        return state.getGridScore() + "";
    }

    @Override
    public String part2(String filename) {
        return "Not today...";
    }
}