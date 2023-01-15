package summit.game.tile;

import summit.game.GameUpdateEvent;

/**
 * 
 * The EmptyTile class is a subclass of the Tile class. It represents an empty
 * tile on the game map that is not passable.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R. S.
 */
public class EmptyTile extends Tile {

    /**
     * 
     * Constructs a new EmptyTile object at the specified x and y coordinates on the
     * game map.
     * 
     * @param x the x coordinate of the EmptyTile object on the game map
     * @param y the y coordinate of the EmptyTile object on the game map
     */
    public EmptyTile(float x, float y) {
        super(x, y);
        super.setBoundary(true);
    }
}
