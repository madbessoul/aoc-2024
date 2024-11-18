package mad.day00;

import mad.Day;
import mad.common.FileUtils;

public class Day00 implements Day {

    @Override
    public String part1(String filename) {
        var data = FileUtils.readLines(filename);
        int total = data.stream().flatMapToInt(String::chars).map(Character::getNumericValue).sum();
        return String.valueOf(total);
    }

    @Override
    public String part2(String filename) {
        return "";
    }
}
