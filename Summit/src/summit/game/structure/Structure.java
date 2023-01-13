package summit.game.structure;

import java.util.ArrayList;

import summit.game.GameUpdateEvent;
import summit.game.entity.Entity;
import summit.game.gamemap.GameMap;
import summit.game.tile.Tile;
import summit.game.tile.TileStack;
import summit.gfx.ColorFilter;
import summit.gfx.Light;
import summit.gfx.OrderPaintEvent;
import summit.gfx.RenderLayers;
import summit.util.GameObject;

/**
 * 
 * The Structure class is an abstract class that represents a structure in the
 * game such as a wall, a chest, or a portal.
 * 
 * This class inherits properties from the GameObject class such as position,
 * size, and the ability to be rendered on the game map.
 * 
 * The Structure class also has a number of its own properties such as shadow
 * and parentMap, and methods such as situate, update and collide.
 * 
 * The Structure class also overrides the setRenderLayer method to update the
 * render layers of the object's shadow.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R.
 */
public abstract class Structure extends GameObject {

    private ColorFilter shadow = new ColorFilter(-60, -60, -60);

    private ArrayList<Light> shadows;

    private GameMap parentMap;

    /**
     * 
     * Creates a new instance of the Structure class.
     * 
     * @param x         the x position of the structure on the parent map
     * @param y         the y position of the structure on the parent map
     * @param width     the width of the structure
     * @param height    the height of the structure
     * @param parentMap the GameMap that the structure is currently on
     */
    public Structure(float x, float y, float width, float height, GameMap parentMap) {
        super(x, y, width, height);
        super.setMoveable(false);
        super.setRenderLayer(RenderLayers.STRUCTURE_ENTITY_LAYER);
        this.situate(parentMap);
        this.parentMap = parentMap;
    }

    /**
     * The setRenderLayer method is responsible for setting the render layer of the
     * Structure object, as well as updating the render layers of the object's
     * shadow.
     * 
     * @param ope the OrderPaintEvent object that contains information about the
     *            current game state and renderer.
     */
    @Override
    public void setRenderLayer(OrderPaintEvent ope) {
        super.setRenderLayer(ope);

        for (Light s : shadows) {
            s.setRenderLayer(ope);
        }
    }

    /**
     * The update method is responsible for updating the state of the Structure
     * object.
     * This implementation does not contain any logic for updating the state of the
     * object, but could be overridden to handle events such as animation or
     * movement.
     * 
     * @param e GameUpdateEvent object that contains information about the current
     *          game state.
     */
    @Override
    public void update(GameUpdateEvent e) throws Exception {
        // do nothing
    }

    /**
     * The collide method is responsible for handling collisions between the
     * Structure object and other entities in the game.
     * This implementation does not contain any logic for collision handling, but
     * could be overridden to handle events such as damage or movement.
     * 
     * @param ev GameUpdateEvent object that contains information about the current
     *           game state.
     * @param e  the Entities object that the Structure object collided with
     */
    @Override
    public void collide(GameUpdateEvent ev, Entity e) {
        // do nothing
    }

    /**
     * The situate method is responsible for setting the shadow of the Structure
     * object on the game map.
     * This method sets the boundaries of the tiles that the Structure object
     * occupies, and creates and adds Light objects to the map to create the shadow
     * effect.
     * 
     * @param map the GameMap object that the Structure object is currently on
     */
    public void situate(GameMap map) {
        shadows = new ArrayList<>();

        TileStack[][] tiles = map.getTiles();

        int startX = Math.round(getX() - getWidth() / 2);
        int startY = Math.round(getY() - getHeight() / 2);

        int endX = Math.round(getX() + getWidth() / 2);
        int endY = Math.round(getY() + getHeight() / 2);

        for (int x1 = startX; x1 < endX; x1++) {
            for (int y1 = startY; y1 < endY; y1++) {
                if (x1 < 0 || x1 > map.getWidth() || y1 < 0 || y1 > map.getHeight())
                    continue;

                Tile t = tiles[y1][x1].topTile();
                t.setBoundary(true);

                Light l = new Light(x1, y1, 1.2f, shadow);
                l.setRenderLayer(RenderLayers.STRUCTURE_ENTITY_LAYER - 1);

                shadows.add(l);
            }
        }
    }

    /**
     * Returns the parent map of the Structure object.
     * 
     * @return GameMap, parentMap.
     */
    public GameMap getParentMap() {
        return this.parentMap;
    }

    /**
     * sets the parent map of the Structure object.
     * 
     * @param parentMap, the parent map of the Structure object
     */
    public void setParentMap(GameMap parentMap) {
        this.parentMap = parentMap;
    }

    /**
     * sets the shadow of the Structure object
     * 
     * @param shadow, the shadow of the Structure object
     */
    public void setShadow(ColorFilter shadow) {
        this.shadow = shadow;
    }
}
