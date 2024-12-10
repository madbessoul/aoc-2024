package mad.day09;

import mad.Day;
import mad.common.FileUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.IntStream;

public class Day09 implements Day {
    private List<String> stringToArray(String s) {
        return s.chars().mapToObj(c -> String.valueOf((char) c)).toList();
    }

    private static List<String> extractDiskData(String filename) {
        return FileUtils.readLines(filename).stream()
                .flatMap(line -> Arrays.stream(line.split("\n")))
                .findFirst()
                .orElseThrow()
                .chars()
                .mapToObj(c -> String.valueOf((char) c))
                .toList();
    }

    private static int getBestFreeSpaceStart(int fileStart, List<String> disk, int fileLength) {
        int bestFreeSpaceStart = -1;
        int currentFreeSpaceStart = -1;
        int currentFreeSpaceLength = 0;

        for (int i = 0; i < fileStart; i++) {
            if (disk.get(i).equals(".")) {
                if (currentFreeSpaceStart == -1) currentFreeSpaceStart = i;
                currentFreeSpaceLength++;
                if (currentFreeSpaceLength >= fileLength && bestFreeSpaceStart == -1) {
                    bestFreeSpaceStart = currentFreeSpaceStart;
                }
            } else {
                currentFreeSpaceStart = -1;
                currentFreeSpaceLength = 0;
            }
        }
        return bestFreeSpaceStart;
    }

    @Override
    public String part1(String filename) {
        List<String> expandedData = extractDiskData(filename);
        System.out.println(expandedData);
        Deque<String> diskQueue = new ArrayDeque<>();

        int fileIndex = 0;
        for (int i = 0; i < expandedData.size(); i++) {
            if (i % 2 == 0) {
                for (int j = 0; j < Long.parseLong(expandedData.get(i)); j++) {
                    diskQueue.add(String.valueOf(fileIndex));
                }
                fileIndex++;
            } else {
                diskQueue.addAll(stringToArray(".".repeat(Integer.parseInt(expandedData.get(i)))));
            }
        }

        System.out.println(diskQueue);
        int idx = 0;
        Long checksum = 0L;

        // Go through the queue, poll from left.
        // When free space, poll from the right
        // Calculate the sum, add and move on until queue is empty
        while (!diskQueue.isEmpty()) {
            String leftValue = diskQueue.pollFirst();
            String rightValue;
            if (!leftValue.equals(".")) {
                checksum += idx * Long.parseLong(leftValue);
            } else {
                while (Objects.equals(rightValue = diskQueue.pollLast(), ".")) {
                }

                if (rightValue != null) {
                    checksum += idx * Long.parseLong(rightValue);
                }
            }
            idx++;
        }
        return String.valueOf(checksum);
    }


    @Override
    public String part2(String filename) {
        List<String> expandedData = extractDiskData(filename);
        List<Pair<String, Integer>> diskMap = new ArrayList<>();

        // disk map with file IDs and lengths
        int fileIndex = 0;
        for (int i = 0; i < expandedData.size(); i++) {
            if (i % 2 == 0) {
                diskMap.add(new ImmutablePair<>(String.valueOf(fileIndex), Integer.parseInt(expandedData.get(i))));
                fileIndex++;
            } else {
                diskMap.add(new ImmutablePair<>(".", Integer.parseInt(expandedData.get(i))));
            }
        }

        // No more queues, grug mode.
        List<String> disk = new ArrayList<>();
        for (Pair<String, Integer> pair : diskMap) {
            disk.addAll(Collections.nCopies(pair.getRight(), pair.getLeft()));
        }

        // Process files from highest ID to lowest
        for (int currentId = fileIndex - 1; currentId >= 0; currentId--) {

            // Current position
            int fileStart = -1;
            int fileLength = 0;
            for (int i = 0; i < disk.size(); i++) {
                if (disk.get(i).equals(String.valueOf(currentId))) {
                    if (fileStart == -1) fileStart = i;
                    fileLength++;
                }
            }

            // Find available space on the left
            int bestFreeSpaceStart = getBestFreeSpaceStart(fileStart, disk, fileLength);

            // Move file if suitable space found, remove from old position and insert at new position
            if (bestFreeSpaceStart != -1) {
                List<String> fileContent = new ArrayList<>(Collections.nCopies(fileLength, String.valueOf(currentId)));
                for (int i = 0; i < fileLength; i++) {
                    disk.set(fileStart + i, ".");
                }
                for (int i = 0; i < fileLength; i++) {
                    disk.set(bestFreeSpaceStart + i, fileContent.get(i));
                }
            }
        }

        // Calculate checksum
        Long checksum = 0L;
        for (int i = 0; i < disk.size(); i++) {
            if (!disk.get(i).equals(".")) {
                checksum += i * Long.parseLong(disk.get(i));
            }
        }

        return String.valueOf(checksum);
    }


}
