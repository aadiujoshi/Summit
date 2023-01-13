package summit.game.tile;

import summit.gfx.Renderer;
import summit.gfx.Sprite;

/**
 * 
 * The WoodPlank class is a subclass of the Tile class. It represents a wooden
 * plank tile on the game map that can be broken.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R.
 */
public class WoodPlank extends Tile {

    /**
     * 
     * Constructs a new WoodPlank object at the specified x and y coordinates on the
     * game map.
     * 
     * @param x the x coordinate of the WoodPlank object on the game map
     * @param y the y coordinate of the WoodPlank object on the game map
     */
    public WoodPlank(float x, float y) {
        super(x, y);
        super.setSprite(Sprite.WOOD_PLANK);
        super.setRenderOp(Renderer.NO_OP);
    }
}
