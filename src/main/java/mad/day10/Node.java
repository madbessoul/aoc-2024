package mad.day10;

import mad.day04.Loc;

import java.util.ArrayList;
import java.util.List;

public class Node {
    Loc location;
    int value;
    List<Node> neighbors;

    Node(Loc location, int value) {
        this.location = location;
        this.value = value;
        this.neighbors = new ArrayList<>();
    }

    @Override
    public String toString()   {
        return "Node{" +
                "location=" + location +
                ", value=" + value +
                ", neighbors=" + neighbors +
                '}';
    }
}

