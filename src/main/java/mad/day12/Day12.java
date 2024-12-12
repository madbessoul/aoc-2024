package mad.day12;

import mad.Day;
import mad.common.FileUtils;
import mad.day04.Loc;

import java.util.*;

public class Day12 implements Day {

    // Told ya, eeeeeeeverything is a goddamn graph
    private static Map<Loc, Plant> buildGraph(Map<Loc, Character> grid) {

        // Graph mapping a location to its Plant (contains value and neighbors)
        Map<Loc, Plant> graph = new HashMap<>();

        // First create all nodes
        for (Map.Entry<Loc, Character> entry : grid.entrySet()) {
            graph.put(entry.getKey(), new Plant(entry.getKey(), entry.getValue()));
        }

        // Find all neighors
        for (Plant current : graph.values()) {
            Loc[] directions = {
                    new Loc(current.location.x() + 1, current.location.y()), // right
                    new Loc(current.location.x() - 1, current.location.y()), // left
                    new Loc(current.location.x(), current.location.y() + 1), // down
                    new Loc(current.location.x(), current.location.y() - 1),  // up
            };

            Loc[] diagonals = {
                    new Loc(current.location.x() - 1, current.location.y() - 1),
                    new Loc(current.location.x() + 1, current.location.y() + 1),
                    new Loc(current.location.x() + 1, current.location.y() - 1),
                    new Loc(current.location.x() - 1, current.location.y() + 1),
            };

            for (Loc nextLoc : directions) {
                Plant next = graph.get(nextLoc);
                if (null != next) { // only not null, don't add outside of the garden
                    current.neighbors.add(next);
                    current.allNeighbors.add(next);
                }
            }

            for (Loc nextLoc : diagonals) {
                Plant next = graph.get(nextLoc);
                if (null != next) { // only not null, don't add outside of the garden
                    current.allNeighbors.add(next);
                }
            }
        }
        return graph;
    }

    private static Map<Loc, Character> gridToMap(List<String> lines) {
        Map<Loc, Character> map = new HashMap<>();
        for (int y = 0; y < lines.size(); y++) {
            for (int x = 0; x < lines.get(y).length(); x++) {
                if (lines.get(y).charAt(x) != '.') {
                    map.put(new Loc(x, y), lines.get(y).charAt(x));
                }
            }
        }
        return map;
    }

    // Use BFS to detect regions. Better than DFS. I think. Maybe.
    private Set<Plant> findPlot(Plant start) {

        Set<Plant> region = new HashSet<>();
        Deque<Plant> queue = new ArrayDeque<>();
        queue.add(start);
        while (!queue.isEmpty()) {
            Plant current = queue.poll();

            // If plant wasn't already in region (returns true if added)
            if (region.add(current)) {
                for (Plant neighbor : current.neighbors) {

                    // If neighbor is same type and not yet processed
                    if (neighbor.value.equals(current.value) && !region.contains(neighbor)) {
                        queue.add(neighbor);
                    }
                }
            }
        }
        return region;
    }

    private List<Plot> findAllPlots(Map<Loc, Plant> graph) {

        List<Plot> regions = new ArrayList<>();
        Set<Plant> visited = new HashSet<>();

        for (Plant plant : graph.values()) {

            // If we haven't visited this plant yet, it must be part of a new region
            if (!visited.contains(plant)) {
                Set<Plant> region = findPlot(plant);
                regions.add(new Plot(region));
                visited.addAll(region);
            }
        }
        return regions;
    }

    @Override
    public String part1(String filename) {
        var garden = gridToMap(FileUtils.readLines(filename));
        var graph = buildGraph(garden);

        List<Plot> regions = findAllPlots(graph);

        Integer totalPrice = regions.stream().map(Plot::getPrice).reduce(0, Integer::sum);

        return String.valueOf(totalPrice);
    }

    @Override
    public String part2(String filename) {
        var garden = gridToMap(FileUtils.readLines(filename));
        var graph = buildGraph(garden);

        List<Plot> regions = findAllPlots(graph);

        Integer totalPrice = regions.stream().map(Plot::getBulkPrice).reduce(0, Integer::sum);

        return String.valueOf(totalPrice);
    }
}
