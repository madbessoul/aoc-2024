package mad.day12;

import mad.day04.Loc;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Plant {
    Loc location;
    Character value;
    List<Plant> neighbors;
    List<Plant> allNeighbors;

    Plant(Loc location, Character value) {
        this.location = location;
        this.value = value;
        this.neighbors = new ArrayList<>();
        this.allNeighbors = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Plant{" +
                "location=" + location +
                ", value=" + value +
                ", neighbors=" + neighbors.stream()
                    .map(n -> n.value.toString())
                    .collect(Collectors.joining(",")) +
                '}';
    }
}
