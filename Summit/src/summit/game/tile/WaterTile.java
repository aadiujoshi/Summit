package summit.game.tile;

import summit.game.GameUpdateEvent;
import summit.game.entity.Entity;
import summit.gfx.Sprite;

/**
 *
 * 
 * The WaterTile class is a subclass of the Tile class. It represents a water
 * tile on the game map that is not passable.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R. S.
 */
public class WaterTile extends Tile {

    /**
     * 
     * Constructs a new WaterTile object at the specified x and y coordinates on the
     * game map.
     * 
     * @param x the x coordinate of the WaterTile object on the game map
     * @param y the y coordinate of the WaterTile object on the game map
     */
    public WaterTile(float x, float y) {
        super(x, y);
        super.setSprite(Sprite.WATER_TILE);
        super.setBoundary(false);
        super.rotateAnimation(true);
        // super.setLight(new Light(x, y, 1f, 0, 0, 100));
    }

    /**
     * 
     * Overrides the collide method of the super class.
     * Sets the inWater attribute of the Entity object to true and onFire attribute
     * to false.
     * 
     * @param ev the GameUpdateEvent object that contains information about the
     *           current game state
     * @param e  the Entity object that is colliding with the WaterTile object
     */
    @Override
    public void collide(GameUpdateEvent ev, Entity e) {
        super.collide(ev, e);
        e.set(Entity.inWater, true);
        e.set(Entity.onFire, false);
    }
}
