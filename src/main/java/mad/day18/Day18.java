package mad.day18;

import mad.Day;
import mad.common.FileUtils;
import mad.day04.Loc;

import java.util.*;

public class Day18 implements Day {
    private record PathState(Loc position, int distance) {}

    // BFS
    private int findShortestPath(Map<Loc, Character> grid, Loc start, Loc end) {
        Queue<PathState> queue = new LinkedList<>();
        Set<Loc> visited = new HashSet<>();

        queue.offer(new PathState(start, 0));
        visited.add(start);

        // Directions, no enums, flemme
        int[] dx = {0, 0, 1, -1};
        int[] dy = {1, -1, 0, 0};

        while (!queue.isEmpty()) {
            PathState current = queue.poll();

            // Over
            if (current.position.equals(end)) {
                return current.distance;
            }

            // Try all four directions
            for (int i = 0; i < 4; i++) {
                Loc next = new Loc(
                        current.position.x() + dx[i],
                        current.position.y() + dy[i]
                );

                // Location exists, valid tile and not visited
                if (grid.containsKey(next) &&
                        grid.get(next) == '.' &&
                        !visited.contains(next)) {

                    queue.offer(new PathState(next, current.distance + 1));
                    visited.add(next);
                }
            }
        }

        return -1;
    }

    private Map<Loc, Character> makeGrid(List<String> lines, int bytes, int width, int height) {
        // Make map of of all locs in a grid 70 x 70 and mark them as '.'
        Map<Loc, Character> grid = new HashMap<>();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                grid.put(new Loc(x, y), '.');
            }
        }

        // Change tiles to '#' for every location in line (up to the numbeber of bytes)
        for (int i =0; i < bytes; i++) {
            Loc loc = new Loc(
                    Integer.parseInt(lines.get(i).split(",")[0]),
                    Integer.parseInt(lines.get(i).split(",")[1])
            );
            grid.put(loc, '#');
        }
        return grid;
    }

    @Override
    public String part1(String filename) {
        List<String> lines = FileUtils.readLines(filename);
        var grid = makeGrid(lines, 1024, 71, 71);
        int shortestPathLength = findShortestPath(grid, new Loc(0, 0), new Loc(70, 70));
        return  shortestPathLength + "";
    }

    @Override
    public String part2(String filename) {
        List<String> lines = FileUtils.readLines(filename);

        Loc blockingLoc = null;
        for (int i = 1024; i < lines.size(); i++) {
            var grid = makeGrid(lines, i, 71, 71);
            int shortestPathLength = findShortestPath(grid, new Loc(0, 0), new Loc(70, 70));
            if (shortestPathLength == -1) {
                blockingLoc = new Loc(
                        Integer.parseInt(lines.get(i-1).split(",")[0]),
                        Integer.parseInt(lines.get(i-1).split(",")[1])
                );
                break;
            }
        }
        return Objects.nonNull(blockingLoc) ? blockingLoc.toString() : "";
    }
}
