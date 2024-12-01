package mad.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class FileUtils {

    public static String getResourceFilePath(String fileName) {
        return Paths.get("/home/mad/dev/aoc-2024/aoc-2024/src/main/resources", fileName).toString();
    }

    public static List<String> readLines(String filename) {
        String fileResource = getResourceFilePath(filename);
        try {
            return Files.readAllLines(Paths.get(fileResource));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Stream<Integer> readInts(String filename) {
        String fileResource = getResourceFilePath(filename);
        return readLines(fileResource).stream().map(Integer::parseInt);
    }

    public static int[][] readGrid(String filename) {
        List<String> lines = readLines(filename);
        int[][] grid = new int[lines.size()][lines.getFirst().length()];
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            for (int j = 0; j < line.length(); j++) {
                grid[i][j] = Character.getNumericValue(line.charAt(j));
            }
        }
        return grid;
    }
}
