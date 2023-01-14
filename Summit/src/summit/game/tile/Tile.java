/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.tile;

import summit.game.GameUpdateEvent;
import summit.game.animation.ParticleAnimation;
import summit.game.entity.Entity;
import summit.gfx.ColorFilter;
import summit.gfx.Light;
import summit.gfx.OrderPaintEvent;
import summit.gfx.PaintEvent;
import summit.gfx.RenderLayers;
import summit.util.GameObject;
import summit.util.ScheduledEvent;
import summit.util.GameScheduler;
import summit.util.GraphicsScheduler;

/**
 * 
 * The Tile class is an abstract class that represents a tile on the game map.
 * It is a subclass of the GameObject class and
 * 
 * serves as the base class for all other tile types such as GrassTile,
 * StoneTile, and LavaTile.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R.
 */
public abstract class Tile extends GameObject {

    public static final int UNBREAKABLE = -1;

    // if true -> entities cannot pass through this tile
    private boolean boundary;
    // private boolean breakable;
    private boolean destroyed;

    private boolean iced;
    private int hitsToBreak;
    private int hits;
    private ColorFilter iceFilter;

    // managed by TileStack;
    // If not null, the tile is pushed to the top of the tile stack
    // private Tile reqPushTile;

    // Rendering hint, nothing to do with gameplay
    private int depth;

    private ScheduledEvent rotateAnim;

    // shut up its necessary
    private boolean animateParticles = false;
    private ParticleAnimation particleAnimation;

    /**
     * 
     * Constructs a new Tile object at the specified x and y coordinates on the game
     * map with the specified width and height.
     * 
     * @param x      the x coordinate of the Tile object on the game map
     * 
     * @param y      the y coordinate of the Tile object on the game map
     * 
     * @param width  the width of the Tile object on the game map
     * 
     * @param height the height of the Tile object on the game map
     */
    public Tile(float x, float y, float width, float height) {
        super(x, y, width, height);
        super.setRenderLayer(RenderLayers.TILE_LAYER);
        super.setOutline(true);
        super.setRenderOp((int) (Math.random() * 5));

        this.hitsToBreak = UNBREAKABLE;
        this.iceFilter = new ColorFilter(0, 0, 50);
    }

    /**
     * 
     * Constructs a new Tile object at the specified x and y coordinates on the game
     * map with the default width and height of 1.
     * 
     * @param x the x coordinate of the Tile object on the game map
     * @param y the y coordinate of the Tile object on the game map
     */
    public Tile(float x, float y) {
        this(x, y, 1, 1);
    }

    /**
     * The update method is called every frame by the game engine. In this method,
     * the Tile object can check for and perform any necessary updates such as
     * checking for collision with other entities or updating its position on the
     * game map.
     *
     * @param e the GameUpdateEvent object containing information about the current
     *          game state and any events that have occurred during the current
     *          frame.
     * @throws Exception if any errors occur during the update process.
     */
    @Override
    public void update(GameUpdateEvent e) throws Exception {
        // cover with snow
        // if(Math.random() < 0.000005)
        // reqPushTile = new SnowTile(getX(), getY());
    }

    /**
     * The paint method is called every frame by the game engine and is used to
     * render the Tile object on the game screen.
     * 
     * @param e the PaintEvent object containing information about the current game
     *          state and any events that have occurred during the current frame.
     */
    @Override
    public void paint(PaintEvent e) {
        if (iced)
            setColorFilter(iceFilter);
        else {
            // reset to default filter
            setColorFilter(getColorFilter());
        }

        super.paint(e);

        // Point p = Renderer.toPixel(getX(), getY(), e.getCamera());

        // e.getRenderer().renderText((int)getX()+"", (int)p.x, (int)p.y-4,
        // Renderer.NO_OP, null);
        // e.getRenderer().renderText((int)getY()+"", (int)p.x, (int)p.y+4,
        // Renderer.NO_OP, null);
    }

    /**
     * Called by the game engine to set the render layer of the Tile object.
     * 
     * @param e the OrderPaintEvent object containing information about the current
     *          game state and any events that have occurred during the current
     *          frame.
     */
    @Override
    public void setRenderLayer(OrderPaintEvent e) {
        super.setRenderLayer(e);

        if (particleAnimation != null)
            particleAnimation.setRenderLayer(e);

        if (particleAnimation != null && particleAnimation.shouldTerminate())
            particleAnimation = null;
    }

    /**
     * Called by the game engine to reinitialize the Tile object.
     */
    @Override
    public void reinit() {
        super.reinit();
        rotateAnimation(rotateAnim != null);
    }

    // make breaking particle animation

    /**
     * Makes breaking particle animation
     * 
     * @param e the GameUpdateEvent object containing information about the current
     *          game state and any events that have occurred during the current
     *          frame.
     */
    public void destroy(GameUpdateEvent e) {
        e.getMap().addAnimation(new ParticleAnimation(getX(), getY(), 500, 15, getColor()));
    }

    /**
     * Called by the game engine when the Tile object collides with another Entity
     * object.
     * 
     * @param ev the GameUpdateEvent object containing information about the current
     *           game state and any events that have occurred during the current
     *           frame.
     * @param e  the Entity object that the Tile object has collided with.
     */
    @Override
    public void collide(GameUpdateEvent ev, Entity e) {
        e.set(Entity.inWater, false);
        e.set(Entity.onFire, false);

        if (e.is(Entity.moving) && animateParticles && particleAnimation == null) {
            particleAnimation = new ParticleAnimation(e.getX(), e.getY(), 500, 15, getColor());
        }
    }

    /**
     * Handle a click event on this Tile object. If the tile is breakable and is
     * iced, the ice will break after 3 hits.
     * If the tile is breakable and not iced, it will be destroyed.
     *
     * @param e the GameUpdateEvent associated with the click event
     */
    @Override
    public void gameClick(GameUpdateEvent e) {
        hits++;

        if (isBreakable()) {
            if (iced && hits == 3)
                this.iced = !iced;

            if (!iced)
                this.setDestroy(true);
        }
    }

    /**
     * 
     * Returns a string representation of the Tile object.
     * The string contains the name of the tile, its x and y coordinates, and its
     * color filter.
     * 
     * @return a string representation of the Tile object
     */
    @Override
    public String toString() {
        return getName() + "  " + getX() + "  " + getY() + "  " + getColorFilter().toString();
    }

    // ------------- getters and setters
    // ----------------------------------------------------------
   
    /**
     * Returns whether or not this Tile object is considered a boundary, which means
     * entities cannot pass through it.
     *
     * @return true if this Tile object is a boundary, false otherwise
     */
    public boolean isBoundary() {
        return this.boundary;
    }

    /**
     * 
     * 
     * 
     * public boolean isBoundary() {
     * return this.boundary;
     * }
     * 
     * /**
     * 
     * Sets whether or not this tile is a boundary tile.
     * Boundary tiles are tiles that entities cannot pass through.
     * 
     * @param bounded true if this tile should be a boundary tile, false otherwise
     */
    public void setBoundary(boolean bounded) {
        this.boundary = bounded;
    }

    /**
     * 
     * Returns whether or not this tile has been destroyed.
     * 
     * @return true if this tile has been destroyed, false otherwise
     */
    public boolean destroyed() {
        return this.destroyed;
    }

    /**
     * Sets if this tile is destroyed.
     *
     * @param destroy the new destroyed state of this tile
     */
    public void setDestroy(boolean destroy) {
        this.destroyed = destroy;
    }

    /**
     * 
     * Sets the light of the tile.
     * 
     * @param light the light to set for the tile
     */
    @Override
    public void setLight(Light light) {
        light.setX(this.getX());
        light.setY(this.getY());
        super.setLight(light);
    }

    /**
     * Returns whether or not this tile is breakable.
     *
     * @return true if this tile is breakable, false otherwise
     */
    public boolean isBreakable() {
        return hitsToBreak != UNBREAKABLE;
    }

    /**
     * Sets if this tile is breakable.
     *
     * @param hitsToBreak the new breakable state of this tile
     */
    public void setBreakable(int hitsToBreak) {
        this.hitsToBreak = hitsToBreak;
    }

    /**
     * Returns the depth of this tile, which is used as a rendering hint.
     *
     * @return the depth of this tile
     */
    public int getDepth() {
        return this.depth;
    }

    /**
     * Sets the depth of this tile, which is used as a rendering hint.
     *
     * @param depth the new depth of this tile
     */
    public void setDepth(int depth) {
        this.depth = depth;
    }

    /**
     * 
     * Enables or disables the particle animation for the tile.
     * 
     * @param a true to enable particle animation, false to disable
     */
    public void particleAnimation(boolean a) {
        this.animateParticles = a;
    }

    /**
     * 
     * Enables or disables the rotation animation for the tile.
     * 
     * @param a true to enable rotation animation, false to disable
     */
    public void rotateAnimation(boolean a) {
        if (a) {
            rotateAnim = new ScheduledEvent(300, ScheduledEvent.FOREVER) {
                @Override
                public void run() {
                    setRenderOp((int) (Math.random() * 5));
                }
            };
            GraphicsScheduler.registerEvent(rotateAnim);
        } else {
            rotateAnim = null;
        }
    }

    /**
     * Returns whether or not this tile is iced.
     *
     * @return true if this tile is iced, false otherwise
     */
    public boolean isIced() {
        return this.iced;
    }

    /**
     * Sets whether or not this tile is iced.
     *
     * @param iced the new iced state of this tile
     */
    public void setIced(boolean iced) {
        this.iced = iced;
        if (iced)
            hits = 0;
    }

    /**
     * Sets the color filter of this tile when it is iced.
     *
     * @param iceFilter the new color filter of this tile when it is iced
     */
    public void setIceFilter(ColorFilter iceFilter) {
        this.iceFilter = iceFilter;
    }

    // public Tile getReqPushTile() {
    // return this.reqPushTile;
    // }

    // public void setReqPushTile(Tile reqPushTile) {
    // this.reqPushTile = reqPushTile;
    // }
}