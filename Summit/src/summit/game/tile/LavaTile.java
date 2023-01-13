/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.tile;

import summit.game.GameUpdateEvent;
import summit.game.entity.Entity;
import summit.gfx.Light;
import summit.gfx.Sprite;

/**
 * 
 * The LavaTile class is a subclass of the Tile class. It represents a lava tile
 * on the game map that damages entities that collides with it.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R.
 */
public class LavaTile extends Tile {

    /**
     * 
     * The dummy variable is used for damaging entities that collide with the
     * LavaTile object.
     */
    private Entity dummy;

    /**
     * 
     * Constructs a new LavaTile object at the specified x and y coordinates on the
     * game map.
     * 
     * The LavaTile object has a sprite of "Sprite.LAVA_TILE", rotate animation is
     * set to true,
     * 
     * and a light(glow) is added to the object.
     * 
     * @param x the x coordinate of the LavaTile object on the game map
     * 
     * @param y the y coordinate of the LavaTile object on the game map
     */
    public LavaTile(float x, float y) {
        super(x, y);
        super.setSprite(Sprite.LAVA_TILE);
        super.rotateAnimation(true);
        Light glow = new Light(x, y, 1f, 255, 163, 0);
        super.setLight(glow);
        Math.random();

        dummy = new Entity(getX(), getY(), 1, 1) {
            @Override
            public void gameClick(GameUpdateEvent e) {
            }
        };
        // dummy.setAttackDamage(1);
        dummy.setHealth(Integer.MAX_VALUE);
    }

    /**
     * 
     * this method is called when an entity collides with the LavaTile object
     * 
     * it damages the entity colliding with the LavaTile object using the dummy
     * variable.
     * 
     * it also sets the colliding entity on fire.
     * 
     * @param ev The GameUpdateEvent that contains the game state
     * 
     * @param e  the entity that is colliding with the LavaTile object
     */
    @Override
    public void collide(GameUpdateEvent ev, Entity e) {
        super.collide(ev, e);
        e.damage(ev, dummy);

        e.set(Entity.onFire, true);
    }
}
