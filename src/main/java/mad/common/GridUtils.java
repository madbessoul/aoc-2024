package mad.common;

import mad.day04.Loc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GridUtils {
    public static Map<Loc, Character> gridToMap(List<String> lines) {
        Map<Loc, Character> map = new HashMap<>();
        for (int y = 0; y < lines.size(); y++) {
            for (int x = 0; x < lines.get(y).length(); x++) {
                map.put(new Loc(x, y), lines.get(y).charAt(x));
            }
        }
        return map;
    }
}
