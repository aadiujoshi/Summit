/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.tile;

import summit.game.GameUpdateEvent;
import summit.gfx.Renderer;
import summit.gfx.Sprite;

/**
 * 
 * The StoneTile class is a subclass of the Tile class. It represents a stone
 * tile on the game map.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R.
 */
public class StoneTile extends Tile {

    /*
     * Constructs a new StoneTile object at the specified x and y coordinates on the
     * game map.
     * 
     * @param x the x coordinate of the StoneTile object on the game map
     * 
     * @param y the y coordinate of the StoneTile object on the game map
     */
    public StoneTile(float x, float y) {
        super(x, y);
        super.setSprite(Sprite.STONE_TILE);
        super.particleAnimation(true);
        super.setColor(Renderer.toIntRGB(100, 100, 100));
    }
}
