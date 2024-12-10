package mad.day10;

import mad.Day;
import mad.common.FileUtils;
import mad.day04.Loc;

import java.util.*;
import java.util.stream.Collectors;

public class Day10 implements Day
{
    private static Map<Loc, Node> buildGraph(Map<Loc, Integer> grid) {
        // Graph mapping a location to its Node (contains value and neighbors)
        // Can't use Loc because I need the values to be able to calculate paths
        Map<Loc, Node> graph = new HashMap<>();

        // First create all nodes
        for (Map.Entry<Loc, Integer> entry : grid.entrySet()) {
            graph.put(entry.getKey(), new Node(entry.getKey(), entry.getValue()));
        }

        // Find all neighors
        for (Node current : graph.values()) {
            Loc[] directions = {
                    new Loc(current.location.x() + 1, current.location.y()), // right
                    new Loc(current.location.x() - 1, current.location.y()), // left
                    new Loc(current.location.x(), current.location.y() + 1), // down
                    new Loc(current.location.x(), current.location.y() - 1)  // up
            };

            // Only connect reachable neigbors
            for (Loc nextLoc : directions) {
                Node next = graph.get(nextLoc);
                if (next != null && next.value == current.value + 1) {
                    current.neighbors.add(next);
                }
            }
        }
        return graph;
    }

    // I should add this to common, I LOVE grids as maps
    private static Map<Loc, Integer> gridToMap(List<String> lines) {
        Map<Loc, Integer> map = new HashMap<>();
        for (int y = 0; y < lines.size(); y++) {
            for (int x = 0; x < lines.get(y).length(); x++) {
                if (lines.get(y).charAt(x) != '.') {
                    map.put(new Loc(x, y), Character.getNumericValue(lines.get(y).charAt(x)));
                }
            }
        }
        return map;
    }

    private static List<List<Node>> findAllPaths(Map<Loc, Node> graph) {
        List<List<Node>> allPaths = new ArrayList<>();

        // Find all starting points (nodes with value 0)
        for (Node start : graph.values()) {
            if (start.value == 0) {
                List<Node> currentPath = new ArrayList<>();
                currentPath.add(start);
                dfs(start, currentPath, allPaths);
            }
        }
        return allPaths;
    }

    private static void dfs(Node current, List<Node> currentPath, List<List<Node>> allPaths) {
        // Stop if we have reached the top
        if (current.value == 9) {
            allPaths.add(new ArrayList<>(currentPath));
            return;
        }

        // Try all neighbors
        for (Node next : current.neighbors) {
            currentPath.add(next);
            dfs(next, currentPath, allPaths);
            currentPath.removeLast();
        }
    }

    @Override
    public String part1(String filename) {
        Map<Loc, Node> graph = buildGraph(gridToMap(FileUtils.readLines(filename)));
        List<List<Node>> paths = findAllPaths(graph);

        Map<Loc, Set<Loc>> pathScores = new HashMap<>();
        for (List<Node> path : paths) {
            Loc start = path.getFirst().location;
            Loc end = path.getLast().location;
            if (!pathScores.containsKey(start)) {
                pathScores.put(start, new HashSet<>());
            }
            pathScores.get(start).add(end);
        }

        return String.valueOf(pathScores.values().stream().map(Set::size).reduce(0, Integer::sum));
    }

    @Override
    public String part2(String filename) {
        Map<Loc, Node> graph = buildGraph(gridToMap(FileUtils.readLines(filename)));
        List<List<Node>> paths = findAllPaths(graph);
        return String.valueOf(paths.size());
    }
}
