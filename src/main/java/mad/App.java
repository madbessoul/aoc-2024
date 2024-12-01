package mad;

import mad.day01.Day01;

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

        int part = 2;
        if(args.length > 1){
            part = Integer.parseInt(args[1]);
        }

        String fileName = makeFilename(day);

        String result;
        if(part == 1) {
            result = DAYS.get(day).part1(fileName);
        } else {
            result = DAYS.get(day).part2(fileName);
        }

        System.out.println(result);
    }


}