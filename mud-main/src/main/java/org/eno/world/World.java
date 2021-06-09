package org.eno.world;

import org.eno.map.Coordinate;

public class World {
    public static final int WORLD_HEIGHT = 3;
    public static final int WORLD_WIDTH = 3;

    private final Space[][] spaces = new Space[WORLD_HEIGHT][WORLD_WIDTH];

    public Coordinate getSpawn() {
        return new Coordinate(1, 0);
    }

    public World() {

        spaces[0][0] = new Space(0, 0, "You see the sea in the south and west. The beach goes on to east and north.");
        spaces[0][1] = new Space(0, 1, "You see the sea in west. Grass in west. The goes on to north and south.");
        spaces[0][2] = new Space(0, 2, "You see the sea in the north and west. The beach goes on to east and south.");

        spaces[1][0] = new Space(1, 0,
                "You see the sea in the south. In north there is grass. The beach goes on to east and west.");
        spaces[1][1] = new Space(1, 1,
                "You see grass all around you.");
        spaces[1][2] = new Space(1, 2, "You see the sea in north. Grass in south. The beach goes on to east and west");

        spaces[2][0] = new Space(2, 0, "You see the sea in the south and east. The beach goes on to north and west.");
        spaces[2][1] = new Space(2, 1, "You see the sea in the east. In west there is grass.  The beach goes on to north and south.");
        spaces[2][2] = new Space(2, 2, "You see the sea in the north and east. The beach goes on to south and west.");

    }

    public Space getSpace(Coordinate coordinate) {
        return spaces[coordinate.getX()][coordinate.getY()];
    }
}
