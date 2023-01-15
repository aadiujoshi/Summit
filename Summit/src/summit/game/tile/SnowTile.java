/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.tile;

import summit.game.GameUpdateEvent;
import summit.game.item.ItemStorage;
import summit.game.item.SnowballItem;
import summit.gfx.ColorFilter;
import summit.gfx.Light;
import summit.gfx.RenderLayers;
import summit.gfx.Renderer;
import summit.gfx.Sprite;

/**
 * 
 * The SnowTile class is a subclass of the Tile class. It represents a snow tile
 * on the game map
 * 
 * that can be broken and drops snowballs.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R. S.
 */
public class SnowTile extends Tile {

    /**
     * 
     * The item storage used to hold the snowballs that will be dropped when the
     * tile is broken.
     */
    private ItemStorage snowballs;

    /**
     * 
     * Constructs a new SnowTile object at the specified x and y coordinates on the
     * game map.
     * The tile is breakable with a breakability of 1 and drops snowballs.
     * 
     * @param x the x coordinate of the SnowTile object on the game map
     * @param y the y coordinate of the SnowTile object on the game map
     */
    public SnowTile(float x, float y) {
        super(x, y);
        super.setSprite(Sprite.SNOW_TILE);
        super.setBreakable(1);
        Light l = new Light(x, y, 1f, 100, 100, 100);
        l.setRenderLayer(RenderLayers.STRUCTURE_ENTITY_LAYER - 1);

        super.setIceFilter(new ColorFilter(-10, -20, 0));

        super.particleAnimation(true);
        super.setColor(Renderer.toIntRGB(170, 214, 230));

        this.snowballs = new ItemStorage(null);

        snowballs.addItems(new SnowballItem(null), (int) (Math.random() * 2));
    }

    /**
     * 
     * Handles the game click event on the SnowTile object. If the tile is
     * breakable,
     * 
     * the player will pick up the snowballs stored in the item storage.
     * 
     * @param e the GameUpdateEvent object that holds information about the game
     *          state.
     */
    @Override
    public void gameClick(GameUpdateEvent e) {
        super.gameClick(e);

        if (isBreakable()) {
            e.getMap().getPlayer().pickupItems(snowballs);
        }
    }

    /**
     * 
     * Reinitializes the SnowTile object.
     */
    @Override
    public void reinit() {
        super.reinit();
        snowballs.reinit();
    }
}