package mad.day12;

import mad.day04.Loc;

import java.util.List;
import java.util.Set;

public class Plot {
    Set<Plant> plants;
    char plantType;
    int area;
    int perimeter;
    int price;
    int sides;
    int bulkPrice;

    Plot(Set<Plant> plants) {
        this.plants = plants;
        this.plantType = plants.iterator().next().value;
        this.area = calculateArea();
        this.perimeter = calculatePerimeter();
        this.price = this.area * this.perimeter;
        this.sides = calculateSides();
        this.bulkPrice = this.area * this.sides;
    }

    // Calculating the number of sides means calculating the numbers of corners
    // For every plant, we need to check if it's an outside or an inside corner
    private int calculateSides() {

        int[][] NE = {{1, -1}, {1, 0}, {0, -1}};
        int[][] SE = {{1, 1}, {1, 0}, {0, 1}};
        int[][] SW = {{-1, 1}, {-1, 0}, {0, 1}};
        int[][] NW = {{-1, -1}, {-1, 0}, {0, -1}} ;

        var corners = List.of(NE, SE, SW, NW);
        int numCorners = 0;

        for (Plant plant: plants) {
            for(int[][] corner : corners) {
                int matches = 0;
                boolean cornerMatch = false;
                for (int i = 0; i <= 2; i++) {
                    Loc loc = new Loc(plant.location.x() + corner[i][0], plant.location.y() + corner[i][1]);
                    Plant neighbor = plant.allNeighbors.stream().filter(n -> n.location.equals(loc)).findFirst().orElse(null);

                    // Match the plot in the corner separately, will help define whether outside or inside corner
                    if (i == 0) {
                        cornerMatch = (neighbor == null || neighbor.value != plantType);
                    }

                    if (neighbor == null) {
                        matches++;
                    } else {
                        if (neighbor.value != plantType) {
                            matches++;
                        }
                    }
                }

                // Matches == 3 : outside corner, nothing in the diagonal neighborhoor
                // Matches == 2 but not cornerMatch : Plot of the same type on the diagonal, still an outside corner though
                // Matches == 1 and cornerMatch : inside corner
                if ((matches == 3) || (!cornerMatch && matches == 2) || (cornerMatch && matches == 1)) {
                    numCorners++;
                }
            }
        }
        return numCorners;

    }

    private int calculateArea() {
        return plants.size();
    }

    private int calculatePerimeter() {
        int totalPerimeter = 0;
        for (Plant plant : plants) {
            int startingPerimeter = 4;
            for (Plant neighbor : plant.neighbors) {
                if (neighbor != null && neighbor.value == plantType) {
                    startingPerimeter--;
                }
            }
            totalPerimeter += startingPerimeter;
        }
        return totalPerimeter;
    }

    public int getPrice() {
        return price;
    }

    public int getBulkPrice() {
        return bulkPrice;
    }

    @Override
    public String toString() {
        return "Plot{" +
                "PlantType=" + plantType +
                ", area=" + area +
                ", perimeter=" + perimeter +
                ", sides=" + sides +
                ", price=" + price +
                ", bulkPrice=" + bulkPrice +
                '}';
    }
}
