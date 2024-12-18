package mad.day17;

import java.util.ArrayList;
import java.util.List;

class Memory {
    Long A;
    Long B;
    Long C;
    List<Integer> result;

    public Memory(Long a, Long b, Long c) {
        A = a;
        B = b;
        C = c;
        result = new ArrayList<>();
    }

    public Long getA() {
        return A;
    }

    public void setA(Long a) {
        A = a;
    }

    public Long getB() {
        return B;
    }

    public void setB(Long b) {
        B = b;
    }

    public Long getC() {
        return C;
    }

    public void setC(Long c) {
        C = c;
    }

    public void addResult(int value) {
        result.add(value);
    }

    public List<Integer> getResult() {
        return result;
    }

    @Override
    public String toString() {
        return String.format("A: %-15d B: %-15d C: %-15d Result: %s",
                A, B, C, result.toString());
    }
}