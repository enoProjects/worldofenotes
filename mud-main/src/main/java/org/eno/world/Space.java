package org.eno.world;

import org.eno.map.Coordinate;

public class Space {

    private final String description;
    private final Coordinate coordinate;

    public Space(int x,
                 int y,
                 String description) {
        coordinate = new Coordinate(x, y);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }
}
