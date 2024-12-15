package mad;

import mad.day01.Day01;
import mad.day02.Day02;
import mad.day03.Day03;
import mad.day04.Day04;
import mad.day05.Day05;
import mad.day06.Day06;
import mad.day07.Day07;
import mad.day08.Day08;
import mad.day09.Day09;
import mad.day10.Day10;
import mad.day11.Day11;
import mad.day12.Day12;
import mad.day13.Day13;
import mad.day14.Day14;
import mad.day15.Day15;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class App {

    private static final Map<Integer, Day> DAYS;

    static {
        DAYS = new HashMap<>();
        DAYS.put(1, new Day01());
        DAYS.put(2, new Day02());
        DAYS.put(3, new Day03());
        DAYS.put(4, new Day04());
        DAYS.put(5, new Day05());
        DAYS.put(6, new Day06());
        DAYS.put(7, new Day07());
        DAYS.put(8, new Day08());
        DAYS.put(9, new Day09());
        DAYS.put(10, new Day10());
        DAYS.put(11, new Day11());
        DAYS.put(12, new Day12());
        DAYS.put(13, new Day13());
        DAYS.put(14, new Day14());
        DAYS.put(15, new Day15());
    }

    private static String makeFilename(int day) {
        String paddedDay = String.valueOf(day);
        if(day < 10) {
            paddedDay = "0" + day;
        }
        return "day" + paddedDay + ".txt";
    }

    public static void main(String[] args) {
        int day = 15;
        if(args.length != 0){
            day = Integer.parseInt(args[0]);
        }
        String fileName = makeFilename(day);


        System.out.println("Day " + day + " Part 1");
        Instant start = Instant.now();
        var partOneResult = DAYS.get(day).part1(fileName);
        Instant end = Instant.now();

        System.out.println(partOneResult);
        System.out.println("Total time: " + Duration.between(start, end).toMillis() + " ms");

        System.out.println("---");
        System.out.println("Day " + day + " Part 2");
        Instant start2 = Instant.now();
        var partTwoResult = DAYS.get(day).part2(fileName);
        Instant end2 = Instant.now();

        System.out.println(partTwoResult);
        System.out.println("Total time: " + Duration.between(start2, end2).toMillis() + " ms");
    }


}