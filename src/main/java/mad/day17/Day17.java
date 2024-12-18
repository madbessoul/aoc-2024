package mad.day17;

import mad.Day;
import mad.common.FileUtils;

import java.util.*;
import java.util.stream.Collectors;

public class Day17 implements Day {


    @Override
    public String part1(String filename) {
        List<String> lines = FileUtils.readLines(filename);

        Memory memory = new Memory(0L, 0L, 0L);
        Program program = new Program(new ArrayList<>(), 0);

        for (String line : lines) {
            if (line.startsWith("Register A")) {
                memory.setA(Long.parseLong(line.split(":")[1].trim()));
            }   else if (line.startsWith("Register B")) {
                memory.setB(Long.parseLong(line.split(":")[1].trim()));
            }   else if (line.startsWith("Register C")) {
                memory.setC(Long.parseLong(line.split(":")[1].trim()));
            }   else if (line.startsWith("Program")) {
                String[] ops = line.split(":")[1].split(",");
                program.setOpseq(Arrays.stream(ops).map(String::trim).map(Integer::parseInt).toList());
            }
        }


        OpEx opex = new OpEx(memory, program, false);
        var result = opex.exec();

        return result.stream().map(String::valueOf).collect(Collectors.joining(","));
    }

    @Override
    public String part2(String filename) {
        List<String> lines = FileUtils.readLines(filename);

        Memory memory = new Memory(0L, 0L, 0L);
        Program program = new Program(new ArrayList<>(), 0);

        for (String line : lines) {
            if (line.startsWith("Register A")) {
                memory.setA(Long.parseLong(line.split(":")[1].trim()));
            }   else if (line.startsWith("Register B")) {
                memory.setB(Long.parseLong(line.split(":")[1].trim()));
            }   else if (line.startsWith("Register C")) {
                memory.setC(Long.parseLong(line.split(":")[1].trim()));
            }   else if (line.startsWith("Program")) {
                String[] ops = line.split(":")[1].split(",");
                program.setOpseq(Arrays.stream(ops).map(String::trim).map(Integer::parseInt).toList());
            }
        }

        OpEx opex = new OpEx(memory, program, false);
        opex.solveA();

        return opex.success + "";
    }
}
