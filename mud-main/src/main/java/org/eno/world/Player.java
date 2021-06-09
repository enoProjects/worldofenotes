package org.eno.world;

import org.eno.map.Coordinate;

public class Player {

    private String name;
    private Coordinate coordinate;

    public Player(String name,
                  Coordinate coordinate) {
        this.name = name;
        this.coordinate = coordinate;
    }

    public String getName() {
        return name;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", " + coordinate +
                '}';
    }
}
