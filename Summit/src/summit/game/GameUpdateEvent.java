/*
 * BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram
 * 2022
 */
package summit.game;

import summit.game.gamemap.GameMap;
import summit.gfx.Renderer;
import summit.gui.Window;

/**
 * The GameUpdateEvent class is used to store information about the current game
 * state, such as the current map, the current window, the current game time,
 * and the mouse's current position. It also includes methods to access and
 * update this information.
 *
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R. S.
 */
public class GameUpdateEvent {

    private Window window;

    private GameMap map;
    private GameWorld world;
    private int deltaTime;

    private int mouseX_pix;
    private int mouseY_pix;

    private float gameX;
    private float gameY;

    private long gametime;

    // simulate if mouse was clicked on this event instance
    private boolean mouseClicked;

    /**
     * 
     * Creates a new instance of the GameUpdateEvent class.
     * 
     * @param world      the current GameWorld
     * @param tickLength the length of the current tick
     */
    public GameUpdateEvent(GameWorld world, long tickLength) {
        this.map = world.getLoadedMap();
        this.world = world;
        this.window = world.getParentWindow();
        this.deltaTime = (int) tickLength;

        this.mouseX_pix = window.mouseX();
        this.mouseY_pix = window.mouseY();

        java.awt.geom.Point2D.Float p = Renderer.toTile(mouseX_pix, mouseY_pix, world.getCamera());

        this.gameX = p.x;
        this.gameY = p.y;

        this.mouseClicked = window.availableClick();
        this.gametime = world.getGametime();
    }

    /**
     * 
     * Sets the current GameMap.
     * 
     * @param m the new GameMap to set
     */
    public void setLoadedMap(GameMap m) {
        world.setLoadedMap(m);
    }

    /**
     * 
     * Returns the current GameMap.
     * 
     * @return GameMap the current GameMap
     */
    public GameMap getMap() {
        return this.map;
    }

    /**
     * 
     * Returns the current game time.
     * 
     * @return long the current game time
     */
    public long gameTime() {
        return this.gametime;
    }

    /**
     * 
     * Returns the time in nanoseconds of the current tick.
     * 
     * @return int the time in nanoseconds of the current tick
     */
    public int getDeltaTimeNS() {
        return this.deltaTime;
    }

    /**
     * 
     * Returns the x position of the mouse in pixels.
     * 
     * @return int the x position of the mouse in pixels
     */
    public int mouseX() {
        return this.mouseX_pix;
    }

    /**
     * 
     * Returns the y position of the mouse in pixels.
     * 
     * @return int the y position of the mouse in pixels
     */
    public int mouseY() {
        return this.mouseY_pix;
    }

    /**
     * 
     * Returns the current GameWorld.
     * 
     * @return GameWorld the current GameWorld
     */
    public GameWorld getWorld() {
        return this.world;
    }

    /**
     * 
     * Returns the x position of the mouse in game coordinates.
     * 
     * @return float the x position of the mouse in game coordinates
     */
    public float gameX() {
        return gameX;
    }

    /**
     * 
     * Returns the y position of the mouse in game coordinates.
     * 
     * @return float the y position of the mouse in game coordinates
     */
    public float gameY() {
        return gameY;
    }

    /**
     * 
     * Returns the current Window.
     * 
     * @return Window the current Window
     */
    public Window getWindow() {
        return this.window;
    }

    /**
     * Returns whether the mouse was clicked during this event.
     *
     * @return boolean true if the mouse was clicked, false otherwise
     */
    public boolean mouseClicked() {
        return this.mouseClicked;
    }
}