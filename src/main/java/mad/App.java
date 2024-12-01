package mad;

import mad.day01.Day01;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class App {

    private static final Map<Integer, Day> DAYS;

    static {
        DAYS = new HashMap<>();
        DAYS.put(1, new Day01());
    }

    private static String makeFilename(int day) {
        String paddedDay = String.valueOf(day);
        if(day < 10) {
            paddedDay = "0" + day;
        }
        return "day" + paddedDay + ".txt";
    }

    public static void main(String[] args) {
        int day = 1;
        if(args.length != 0){
            day = Integer.parseInt(args[0]);
        }
        String fileName = makeFilename(day);


        System.out.println("Day " + day + " Part 1");
        Instant start = Instant.now();
        var partOneResult = DAYS.get(day).part1(fileName);
        Instant end = Instant.now();

        System.out.println(partOneResult);
        System.out.println("Time: " + Duration.between(start, end).toMillis() + " ms");

        System.out.println("Day " + day + " Part 1");
        Instant start2 = Instant.now();
        var partTwoResult = DAYS.get(day).part2(fileName);
        Instant end2 = Instant.now();

        System.out.println(partTwoResult);
        System.out.println("Time: " + Duration.between(start2, end2).toMillis() + " ms");
    }


}