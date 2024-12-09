package mad.day06;

import mad.Day;
import mad.common.FileUtils;
import mad.day04.Loc;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Day06 implements Day {

    private Pair<Map<Loc, Integer>, Loc> makeGridMap(List<String> lines) {
        Map<Loc, Integer> grid = new HashMap<>();
        Loc startingLoc = null;
        for (int j = 0; j < lines.size(); j++) {
            String line = lines.get(j);
            for (int i = 0; i < line.length(); i++) {
                if (line.charAt(i) == '^' && startingLoc == null) {
                    startingLoc = new Loc(i, j);
                }
                if (line.charAt(i) == '#') {
                    grid.put(new Loc(i, j), 1);
                } else {
                    grid.put(new Loc(i, j), 0);
                }
            }
        }
        return ImmutablePair.of(grid, startingLoc);
    }

    private char turnRight(char currentDirection) {
        char[] directions = new char[]{'N', 'E', 'S', 'W'};
        return switch (currentDirection) {
            case 'N' -> directions[1];
            case 'E' -> directions[2];
            case 'S' -> directions[3];
            case 'W' -> directions[0];
            default -> throw new RuntimeException("Invalid direction: " + currentDirection);
        };
    }
    
    private Pair<Loc, Character> moveOneStep(Map<Loc, Integer> grid, Loc loc, char direction) {
        Loc nextLoc;
        char currentDirection = direction;
        do {
            switch (currentDirection) {
                case 'N' -> nextLoc = new Loc(loc.x(), loc.y() - 1);
                case 'E' -> nextLoc = new Loc(loc.x() + 1, loc.y());
                case 'S' -> nextLoc = new Loc(loc.x(), loc.y() + 1);
                case 'W' -> nextLoc = new Loc(loc.x() - 1, loc.y());
                default -> throw new RuntimeException("Unturnable turn");
            }

            // *queue Wilhem scream*
            if (!grid.containsKey(nextLoc)) {
                throw new IndexOutOfBoundsException("Guard has left the chat");
            }

            // *bonk into wall*
            if (grid.get(nextLoc) == 1) {
                currentDirection = turnRight(currentDirection);
            }
        } while (grid.get(nextLoc) == 1);

        return ImmutablePair.of(nextLoc, currentDirection);

    }

    @Override
    public String part1(String filename) {

        var parsedGrid = makeGridMap(FileUtils.readLines(filename));
        var grid = parsedGrid.getLeft();
        var startingLoc = parsedGrid.getRight();

        var visited = new HashSet<Loc>();

        var currentDirection = 'N';
        var currentLoc = startingLoc;
        visited.add(startingLoc);

        boolean stopFlag = false;
        while(!stopFlag) {

            try{
                // Move and get new position and new direction
                var nextStep = moveOneStep(grid, currentLoc, currentDirection);
                currentLoc = nextStep.getLeft();
                currentDirection = nextStep.getRight();

                // Also mark the location as visited
                visited.add(nextStep.getLeft());
            } catch (IndexOutOfBoundsException e) {
                // Stop if we step out of the grid
                stopFlag = true;
            }

            // Also stop if we have visited the whole grid (just in case)
            if (grid.size() == visited.size()) {
                stopFlag = true;
            }
        }

        return String.valueOf(visited.size());
    }

    private boolean testObstacleOnNextStep(Map<Loc, Integer> grid, Loc startingLoc, Loc obstacleLoc) {

        Map<Loc, Integer> gridWithObstacle = new HashMap<>(grid);

        gridWithObstacle.putAll(grid);
        gridWithObstacle.put(obstacleLoc, 1);


        // We start back at the beginning and going North, the loop isn't always a rectangle
        var currentLoc = startingLoc;
        var currentDirection = 'N';

        boolean stopflag = false;
        var visited = new HashMap<Loc, Character>();
        visited.put(currentLoc, currentDirection);

        while(!stopflag) {
            try{
                var nextStep = moveOneStep(gridWithObstacle, currentLoc, currentDirection);
                currentLoc = nextStep.getLeft();
                currentDirection = nextStep.getRight();

                // If we come back to the same location and in the same direction, it means we've looped
                if (visited.containsKey(currentLoc) && visited.get(currentLoc).equals(currentDirection)) {
                    return true;
                }

                visited.put(currentLoc, currentDirection);
            } catch (IndexOutOfBoundsException e) {
                stopflag = true;
            }
        }
        return false;
    }

    @Override
    public String part2(String filename) {

        // What do you mean DRY... we like it WET ! (also I'm lazy)
        var parsedGrid = makeGridMap(FileUtils.readLines(filename));
        var grid = parsedGrid.getLeft();
        var startingLoc = parsedGrid.getRight();

        var possibleObstacles = new HashSet<Loc>();

        var currentDirection = 'N';
        var currentLoc = startingLoc;
        boolean stopFlag = false;

        while(!stopFlag) {

            try{
                // Move and get new position and new direction
                var nextStep = moveOneStep(grid, currentLoc, currentDirection);

                // Test only if next location hasnn't been tested before, also if it's not the starting location
                if (!possibleObstacles.contains(nextStep.getLeft()) && !nextStep.getLeft().equals(startingLoc)) {

                    // We only test the next location on our path, no need to test every location
                    if (testObstacleOnNextStep(grid, startingLoc, nextStep.getLeft())) {
                        possibleObstacles.add(nextStep.getLeft());
                    }
                }
                currentLoc = nextStep.getLeft();
                currentDirection = nextStep.getRight();
            } catch (IndexOutOfBoundsException e) {
                // Stop if we step out of the grid
                stopFlag = true;
            }
        }
        return String.valueOf(possibleObstacles.size());
    }


}
