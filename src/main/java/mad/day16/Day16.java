package mad.day16;

import mad.Day;
import mad.common.Direction;
import mad.common.FileUtils;
import mad.common.GridUtils;
import mad.day04.Loc;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class Day16 implements Day {
    record PathResult (List<Loc> path, int cost){}

    static class State implements Comparable<State> {
        Loc position;
        Direction direction;
        int cost;
        List<Loc> path;

        State(Loc position, Direction direction, int cost, List<Loc> path) {
            this.position = position;
            this.direction = direction;
            this.cost = cost;
            this.path = path;
        }

        @Override
        public int compareTo(State other) {
            return Integer.compare(this.cost, other.cost);
        }
    }

    private static List<Direction> getPossibleDirections(Direction current) {
        List<Direction> directions = new ArrayList<>();
        switch (current) {
            case NORTH -> {
                directions.add(Direction.NORTH); // straight
                directions.add(Direction.WEST);  // left
                directions.add(Direction.EAST);  // right
            }
            case SOUTH -> {
                directions.add(Direction.SOUTH);
                directions.add(Direction.EAST);
                directions.add(Direction.WEST);
            }
            case EAST -> {
                directions.add(Direction.EAST);
                directions.add(Direction.NORTH);
                directions.add(Direction.SOUTH);
            }
            case WEST -> {
                directions.add(Direction.WEST);
                directions.add(Direction.SOUTH);
                directions.add(Direction.NORTH);
            }
        }
        return directions;
    }

    private static void printPath(Map<Loc, Character> grid, List<Loc> path) {

        int minX = grid.keySet().stream().mapToInt(Loc::x).min().getAsInt();
        int maxX = grid.keySet().stream().mapToInt(Loc::x).max().getAsInt();
        int minY = grid.keySet().stream().mapToInt(Loc::y).min().getAsInt();
        int maxY = grid.keySet().stream().mapToInt(Loc::y).max().getAsInt();

        String RED = "\u001B[31m";
        String GREEN = "\u001B[32m";
        String ORANGE = "\u001B[33m";
        String RESET = "\u001B[0m";

        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                Loc currentLoc = new Loc(x, y);
                if (path.contains(currentLoc) &&
                        path.indexOf(currentLoc) != path.size() - 1 &&
                        path.indexOf(currentLoc) != 0) {
                    System.out.print(GREEN  + '*' + RESET);
                } else {
                    Character c = grid.getOrDefault(currentLoc, null);
                    switch (c) {
                        case 'E' -> System.out.print(ORANGE + 'E' + RESET);
                        case 'S' -> System.out.print(ORANGE + 'S' + RESET);
                        case '#' -> System.out.print(RED + c + RESET);
                        case '.' -> System.out.print(' ');
                        default -> System.out.print(c);
                    }
                }
            }
            System.out.println();
        }
    }

    // Modified Djikstra
    private static List<PathResult> findPath(Map<Loc, Character> grid, Loc start) {
        PriorityQueue<State> queue = new PriorityQueue<>();
        Map<Pair<Loc, Direction>, Integer> visited = new HashMap<>();

        List<PathResult> bestPaths = new ArrayList<>();
        int bestCost = Integer.MAX_VALUE;

        queue.offer(new State(start, Direction.EAST, 0, new ArrayList<>(List.of(start))));

        while (!queue.isEmpty()) {
            State current = queue.poll();
            Pair<Loc, Direction> currentKey = new ImmutablePair<>(current.position, current.direction);// We've already been here, skip

            // Skip only if score is strictly worse. If it's the same, we still want to explore
            if (visited.containsKey(currentKey) && (visited.get(currentKey) < current.cost)) {
                continue;
            }

            visited.put(currentKey, current.cost);

            // Finished
            if (grid.get(current.position) == 'E') {
                if (current.cost < bestCost) {
                    // Found a better path, clear previous ones
                    bestPaths.clear();
                    bestCost = current.cost;
                }
                if (current.cost == bestCost) {
                    // Keep this path bien au chaud
                    // Ou au frais
                    bestPaths.add(new PathResult(current.path, current.cost));
                }
            }

            for (Direction nextDirection : getPossibleDirections(current.direction)) {
                Loc nextPosition = new Loc(
                        current.position.x() + nextDirection.dx,
                        current.position.y() + nextDirection.dy
                );

                // If next direction is a wall, skip
                if (!grid.containsKey(nextPosition) || grid.get(nextPosition) == '#') {
                    continue;
                }

                // Move forward = 1
                // Turn + move = 1001
                int additionalCost = (nextDirection == current.direction) ? 1 : 1001;
                int newCost = current.cost + additionalCost;

                List<Loc> newPath = new ArrayList<>(current.path);
                newPath.add(nextPosition);

                queue.offer(new State(nextPosition, nextDirection, newCost, newPath));
            }
        }

        return bestPaths;
    }

    private static List<PathResult> computeBestPath(String filename) {
        List<String> lines = FileUtils.readLines(filename);
        var grid = GridUtils.gridToMap(lines);
        var startLoc= grid.entrySet().stream()
                    .filter(e -> e.getValue() == 'S')
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElseThrow();

        return findPath(grid, startLoc);
    }

    @Override
    public String part1(String filename) {
        var bestPaths = computeBestPath(filename);
        return bestPaths.stream().mapToInt(PathResult::cost).mapToObj(String::valueOf).findFirst().orElseThrow();
    }

    @Override
    public String part2(String filename) {
        var bestPaths = computeBestPath(filename);
        Long numLocs = bestPaths.stream()
                .map(p -> p.path)
                .flatMap(List::stream)
                .distinct()
                .count();
        return numLocs + "";
    }
}
