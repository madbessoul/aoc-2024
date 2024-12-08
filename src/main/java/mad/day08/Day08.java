package mad.day08;

import mad.Day;
import mad.common.FileUtils;
import mad.day04.Loc;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.Stream;

public class Day08 implements Day {

    Map<Character, List<Loc>> findAntennas(List<String> lines) {

        var antennas = new HashMap<Character, List<Loc>>();

        for (int j = 0; j < lines.size(); j++) {
            String line = lines.get(j);
            for (int i = 0; i < line.length(); i++) {
                if (line.charAt(i) != '.') {
                    if (!antennas.containsKey(line.charAt(i))) {
                        antennas.put(line.charAt(i), new ArrayList<>(List.of(new Loc(i, j))));
                    } else {
                        antennas.get(line.charAt(i)).add(new Loc(i, j));
                    }
                }
            }
        }
        return antennas;
    }

    private List<Pair<Loc, Loc>> generateUniquePairs(List<Loc> antennaLocs) {
        List<Pair<Loc, Loc>> uniqueSublists = new ArrayList<>();

        // If list is too short to create sublists, return empty list
        if (antennaLocs.size() < 2) {
            return List.of();
        }

        // Should have used apaches commons for this
        for (int i = 0; i < antennaLocs.size() - 1; i++) {
            for (int j = i + 1; j < antennaLocs.size(); j++) {
                Pair<Loc, Loc> sublist = ImmutablePair.of(antennaLocs.get(i), antennaLocs.get(j));
                uniqueSublists.add(sublist);
            }
        }

        return uniqueSublists;
    }

    private List<Loc> computeAntinodesForPair(Pair<Loc, Loc> antennaPair, Integer width, Integer height) {

        // Oh there has to be a better way
        var deltaX = antennaPair.getRight().x() - antennaPair.getLeft().x();
        var deltaY = antennaPair.getRight().y() - antennaPair.getLeft().y();

        var firstAntinode = new Loc(antennaPair.getLeft().x() - deltaX, antennaPair.getLeft().y() - deltaY);
        var secondAntinode = new Loc(antennaPair.getRight().x() + deltaX, antennaPair.getRight().y() + deltaY);
        return new ArrayList<>(List.of(firstAntinode, secondAntinode));
    }

    private List<Loc> computeAntinodesForPairPart2(Pair<Loc, Loc> antennaPair, Integer width, Integer height) {

        var deltaX = antennaPair.getRight().x() - antennaPair.getLeft().x();
        var deltaY = antennaPair.getRight().y() - antennaPair.getLeft().y();
        var antinodeList = new ArrayList<Loc>();

        // Okay so any pair is now an antenna huh...
        antinodeList.add(antennaPair.getLeft());
        antinodeList.add(antennaPair.getRight());

        // We look in one direction...
        int step = 1;
        while (true) {
            var antinode = new Loc(
                    antennaPair.getRight().x() + (step * deltaX),
                    antennaPair.getRight().y() + (step * deltaY));

            // Stop if we go out of bounds
            if (antinode.x() < width && antinode.y() < height && antinode.x() >= 0 && antinode.y() >= 0) {
                antinodeList.add(antinode);
                step++;
            } else {break;}
        }

        // reset step count go the othe rway
        step = 1;
        while(true) {
            var antinode = new Loc(
                    antennaPair.getLeft().x() - (step * deltaX),
                    antennaPair.getLeft().y() - (step * deltaY));
            if (antinode.x() < width && antinode.y() < height && antinode.x() >= 0 && antinode.y() >= 0) {
                antinodeList.add(antinode);
                step++;
            } else {break;}
        }

        return antinodeList;
    }

    private boolean isValidAntinode(Loc antinode, Integer width, Integer height) {
        return antinode.x() >= 0 && antinode.x() < width && antinode.y() >= 0 && antinode.y() < height;
    }

    @Override
    public String part1(String filename) {
        List<String> lines = FileUtils.readLines(filename);
        var width = lines.getFirst().length();
        var height = lines.size();
        var antennas = findAntennas(lines);
        Long numAntinodes = antennas.values().stream()
                .map(this::generateUniquePairs)
                .flatMap(List::stream)
                .map(p -> computeAntinodesForPair(p, width, height))
                .flatMap(List::stream)
                .filter(loc -> isValidAntinode(loc, width, height))
                .distinct()
                .count();

        return String.valueOf(numAntinodes);
    }


    @Override
    public String part2(String filename) {
        List<String> lines = FileUtils.readLines(filename);
        var width = lines.getFirst().length();
        var height = lines.size();
        var antennas = findAntennas(lines);
        Long numAntinodes = antennas.values().stream()
                .map(this::generateUniquePairs)
                .flatMap(List::stream)
                .map(p -> computeAntinodesForPairPart2(p, width, height))
                .flatMap(List::stream)
                .filter(loc -> isValidAntinode(loc, width, height))
                .distinct()
                .count();

        return String.valueOf(numAntinodes);
    }
}
