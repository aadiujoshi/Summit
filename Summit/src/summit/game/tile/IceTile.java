package summit.game.tile;

import summit.gfx.Renderer;
import summit.gfx.Sprite;

/**
 *
 * 
 * The IceTile class is a subclass of the Tile class. It represents an icy tile
 * on the game map.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R.
 */
public class IceTile extends Tile {

    /**
     *
     * 
     * Constructs a new IceTile object at the specified x and y coordinates on the
     * game map.
     * 
     * @param x the x coordinate of the IceTile object on the game map
     * @param y the y coordinate of the IceTile object on the game map
     */
    public IceTile(float x, float y) {
        super(x, y);
        super.setBreakable(3);
        super.setSprite(Sprite.ICE_TILE);
        super.setRenderOp(Renderer.NO_OP);
    }
}
