package mad.day17;

import java.util.List;

public class Program {
    List<Integer> opseq;
    int pointer;

    public Program(List<Integer> opseq, int pointer) {
        this.opseq = opseq;
        this.pointer = pointer;
    }

    public List<Integer> next() {
        if (pointer + 1 >= opseq.size()) {
            return null;
        }

        List<Integer> nextOp = List.of(
                opseq.get(pointer),
                opseq.get(pointer + 1)
        );
        pointer += 2;
        return nextOp;
    }

    public boolean peek() {
        return pointer + 1 < opseq.size();
    }

    public List<Integer> getOpseq() {
        return opseq;
    }

    public void setOpseq(List<Integer> opseq) {
        this.opseq = opseq;
    }

    public int getPointer() {
        return pointer;
    }

    public void setPointer(int pointer) {
        this.pointer = pointer;
    }

    @Override
    public String toString() {
        return "opseq: " + opseq + "\npointer: " + pointer;
    }
}
