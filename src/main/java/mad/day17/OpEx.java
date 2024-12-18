package mad.day17;
import java.util.List;

public class OpEx {
    Memory memory;
    Program program;
    boolean verbose;
    Long bestValue;

    public OpEx(Memory memory, Program program, boolean verbose) {
        this.memory = memory;
        this.program = program;
        this.verbose = verbose;

        // ~inf
        this.bestValue = Long.MAX_VALUE;
    }

    private String getOperationName(int opcode) {
        return switch (opcode) {
            case 0 -> "adv";
            case 1 -> "bxl";
            case 2 -> "bst";
            case 3 -> "jnz";
            case 4 -> "bxc";
            case 5 -> "out";
            case 6 -> "bdv";
            case 7 -> "cdv";
            default -> throw new IllegalArgumentException("Invalid opcode: " + opcode);
        };
    }

    void dfs(List<Integer> prog, long currentValue, int position) {
        for (int i = 0; i < 8; i++) {
            program.setPointer(0);
            long newValue = (currentValue * 8) + i;
            this.memory = new Memory(
                    (currentValue * 8) + i,
                    0L,
                    0L
            );

            List<Integer> execResult = exec();

            if (!execResult.equals(prog.subList(prog.size() - position - 1, prog.size()))) {
                continue;
            }

            // update success if current value is better
            if (position == prog.size() - 1) {
                this.bestValue = Math.min(bestValue, newValue);
                return;
            }

            dfs(prog, newValue, position + 1);
        }
    }

    public void solveA() {
        dfs(program.opseq, 0, 0);
    }

    private void runOp(int opcode, int operand) {
        switch (opcode) {
            case 0 -> adv(operand);
            case 1 -> bxl(operand);
            case 2 -> bst(operand);
            case 3 -> jnz(operand);
            case 4 -> bxc();
            case 5 -> out(operand);
            case 6 -> bdv(operand);
            case 7 -> cdv(operand);
        }
    }

    public List<Integer> exec() {
        while (program.peek()) {
            List<Integer> nextOp = program.next();
            int opcode = nextOp.get(0);
            int operand = nextOp.get(1);
            runOp(opcode, operand);
            if (this.verbose) {
                System.out.println(getOperationName(opcode) + " [" + opcode + "," + operand + "] -> " + memory);
            }
        }
        return this.memory.getResult();
    }

    private Long comboValue(int operand) {
        return switch (operand) {
            case 0, 1, 2, 3 -> (long) operand;
            case 4 -> this.memory.A;
            case 5 -> this.memory.B;
            case 6 -> this.memory.C;
            default -> throw new IllegalArgumentException("Invalid operand: " + operand);
        };
    }

    private void adv(int operand) {
        // Division then store in register A
        Long num = memory.getA();
        double denom = Math.pow(2, comboValue(operand));
        memory.setA((long) Math.floor(num / denom));
    }

    private void bxl(int operand) {
        // Bitwise xor between register B and operand
        memory.setB(memory.getB() ^ operand);
    }

    private void bst(int operand) {
        // Combo modulo 8 into B
        memory.setB(comboValue(operand) % 8);
    }

    private void jnz(int operand) {
        // Jump to address in literal operand if register A is not zero
        if (memory.getA() != 0) {
            program.setPointer(operand);
        }
    }

    private void bxc() {
        // Bitwise XOR between register B and register C, store in register B
        memory.setB(memory.getB() ^ memory.getC());
    }

    private void out(int operand) {
        // Combo operand modulo 8 and adds to result
        memory.addResult((int) (comboValue(operand) % 8));
    }

    private void bdv(int operand) {
        // Division then store in register B
        Long num = memory.getA();
        double denom = Math.pow(2, comboValue(operand));
        memory.setB((long) Math.floor(num / denom));
    }

    private void cdv(int operand) {
        // Division then store in register C
        Long num = memory.getA();
        double denom = Math.pow(2, comboValue(operand));
        memory.setC((long) Math.floor(num / denom));
    }
}
