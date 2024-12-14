package mad.day14;

import mad.Day;
import mad.common.FileUtils;
import mad.day04.Loc;
import org.apache.commons.lang3.tuple.Pair;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Day14 implements Day {
    private void printGridToFile(List<Loc> positions, int boxwidth, int boxheight, String outputFile) {
        char[][] grid = new char[boxheight][boxwidth];

        for (int y = 0; y < boxheight; y++) {
            for (int x = 0; x < boxwidth; x++) {
                grid[y][x] = ' ';
            }
        }

        // Mark positions with dots
        positions.forEach(pos -> grid[pos.y()][pos.x()] = '•');

        // Write to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            for (int y = 0; y < boxheight; y++) {
                writer.write(new String(grid[y]));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    List<Integer> countRobotsPerQuadrant(List<Loc> positions, int boxwidth, int boxheight) {
        List<Integer> counts = new ArrayList<>(List.of(0, 0, 0, 0));
        int halfWidth = boxwidth / 2;
        int halfHeight = boxheight / 2;

        positions.forEach(position -> {
            int x = position.x();
            int y = position.y();
            if (x < halfWidth && y < halfHeight) {
                counts.set(0, counts.get(0) + 1);
            } else if (x < halfWidth && y > halfHeight) {
                counts.set(1, counts.get(1) + 1);
            } else if (x > halfWidth && y < halfHeight) {
                counts.set(2, counts.get(2) + 1);
            } else if (x > halfWidth && y > halfHeight) {
                counts.set(3, counts.get(3) + 1);
            }
        });
        
        return counts;
    }

    // Calculate new position based on toroidal movement (just like in Pacman! :D)
    Loc computeNewPosition(Pair<Loc, Loc> robotInfo, int boxwidth, int boxheight, int repeats) {
        int dx = robotInfo.getRight().x() * repeats;
        int dy = robotInfo.getRight().y() * repeats;

        int newX = robotInfo.getLeft().x() + dx;
        int newY = robotInfo.getLeft().y() + dy;

        // First module, can get negative number
        // Add box length to make positive
        // Module again if it wasn't negative
        newX = ((newX % boxwidth) + boxwidth) % boxwidth;
        newY = ((newY % boxheight) + boxheight) % boxheight;

        return new Loc(newX, newY);
    }

    Pair<Loc, Loc> parseLine(String line) {
        String[] parts = line.split(" ");
        String[] position = parts[0].substring(2).split(",");
        String[] velocity = parts[1].substring(2).split(",");
        return Pair.of(
                new Loc(Integer.parseInt(position[0]), Integer.parseInt(position[1])),
                new Loc(Integer.parseInt(velocity[0]), Integer.parseInt(velocity[1]))
        );
    }

    // This was useless...
    private boolean gridIsSymmetrical(List<Loc> positions, int boxwidth, int boxheight) {
        boolean isOddWidth = boxwidth % 2 != 0;
        int middle = boxwidth / 2;

        for (int y = 0; y < boxheight; y++) {
            int finalY = y;
            List<Integer> xPositions = positions.stream()
                    .filter(p -> p.y() == finalY)
                    .map(Loc::x)
                    .sorted()
                    .toList();

            if (xPositions.isEmpty()) continue;

            for (int x : xPositions) {
                if (isOddWidth && x == middle) continue;

                int mirrorX = boxwidth - 1 - x;
                if (!xPositions.contains(mirrorX)) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public String part1(String filename) {
        int boxwidth = 101;
        int boxheight = 103;

        var score = countRobotsPerQuadrant(
                FileUtils.readLines(filename).stream()
                .map(this::parseLine)
                .map(pair -> computeNewPosition(pair, boxwidth, boxheight, 100)).toList(),
                boxwidth,
                boxheight
        ).stream().reduce(1, (a, b) -> a * b);

        return score + "";
    }

    @Override
    public String part2(String filename) {
        int boxwidth = 101;
        int boxheight = 103;
        // Arbitrary 10000 limit...
        for (int repeats = 0; repeats <= 10000; repeats++) {
            int finalRepeats = repeats;

            var positions = FileUtils.readLines(filename).stream()
                    .map(this::parseLine)
                    .map(pair -> computeNewPosition(pair, boxwidth, boxheight, finalRepeats))
                    .toList();

            var quads = countRobotsPerQuadrant(positions, boxwidth, boxheight
            );

            // If any quadrant has more than the average * 1.5 positions, we look at it...
            if (quads.stream().anyMatch(i -> i > 180)) {
                printGridToFile(positions, boxwidth, boxheight, "C:/Users/Mad/dev/repeats/day14-grid" + repeats + ".txt");
            }
        }
        return "";
    }
}
