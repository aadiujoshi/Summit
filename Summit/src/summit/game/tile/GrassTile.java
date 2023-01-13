
package summit.game.tile;

import summit.game.GameUpdateEvent;
import summit.gfx.Sprite;

/**
 *
 * 
 * The GrassTile class is a subclass of the Tile class. It represents a grassy
 * tile on the game map.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R.
 */
public class GrassTile extends Tile {

    /**
     *
     * 
     * Constructs a new GrassTile object at the specified x and y coordinates on the
     * game map.
     * 
     * @param x the x coordinate of the GrassTile object on the game map
     * @param y the y coordinate of the GrassTile object on the game map
     */
    public GrassTile(float x, float y) {
        super(x, y);
        super.setSprite(Sprite.GRASS_TILE);

    }
}
