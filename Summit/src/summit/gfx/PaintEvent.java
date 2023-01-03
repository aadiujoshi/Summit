
package summit.gfx;

import summit.game.GameWorld;
import summit.game.gamemap.GameMap;
import summit.gui.Window;

/**
 * Represents an event when the game world needs to be painted on the screen.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R. S.
 */
public class PaintEvent {

    private GameWorld world;
    private GameMap curMap;

    private Renderer renderer;
    private long lastFrame;
    private Camera camera;
    private Window window;

    private int mouseX_pix;
    private int mouseY_pix;

    private float gameX;
    private float gameY;

    /**
     * Constructs a new PaintEvent.
     * 
     * @param world     the GameWorld object that the event belongs to
     * @param window    the Window object that the event belongs to
     * @param renderer  the Renderer object that the event belongs to
     * @param lastFrame the time of the last frame
     * @param mouseXpix the x coordinate of the mouse in pixels
     * @param mouseYpix the y coordinate of the mouse in pixels
     */
    public PaintEvent(GameWorld world, Window window, Renderer renderer, long lastFrame, int mouseXpix, int mouseYpix) {
        this.renderer = renderer;
        this.lastFrame = lastFrame;
        this.window = window;

        this.mouseX_pix = mouseXpix;
        this.mouseY_pix = mouseYpix;

        if (world != null) {
            this.camera = world.getCamera().clone();
            this.curMap = world.getLoadedMap();

            java.awt.geom.Point2D.Float p = Renderer.toTile(mouseX_pix, mouseY_pix, world.getCamera());

            this.gameX = p.x;
            this.gameY = p.y;
        }
    }

    /**
     * Returns the Renderer object associated with this event.
     * 
     * @return the Renderer object
     */
    public Renderer getRenderer() {
        return this.renderer;
    }

    /**
     * Returns the time of the last frame.
     * 
     * @return the time of the last frame
     */
    public long getLastFrame() {
        return this.lastFrame;
    }

    /**
     * Returns the Camera object associated with this event.
     * 
     * @return the Camera object
     */
    public Camera getCamera() {
        return this.camera;
    }

    /**
     * Returns the Window object associated with this event.
     * 
     * @return the Window object
     */
    public Window getWindow() {
        return this.window;
    }

    /**
     * Returns the x coordinate of the mouse in pixels.
     * 
     * @return the x coordinate of the mouse in pixels
     */
    public int mouseX() {
        return this.mouseX_pix;
    }

    /**
     * Returns the y coordinate of the mouse in pixels.
     * 
     * @return the y coordinate of the mouse in pixels
     */

    public int mouseY() {
        return this.mouseY_pix;
    }

    /**
     * Returns the x coordinate of the mouse in the game world.
     * 
     * @return the x coordinate of the mouse in the game world
     */
    public float gameX() {
        return gameX;
    }

    /**
     * Returns the y coordinate of the mouse in the game world.
     * 
     * @return the y coordinate of the mouse in the game world
     */
    public float gameY() {
        return gameY;
    }

    /**
     * Returns the GameWorld object associated with this event.
     * 
     * @return the GameWorld object
     */
    public GameWorld getWorld() {
        return this.world;
    }

    /**
     * Returns the GameMap object that is currently loaded in the game world.
     * 
     * @return the GameMap object
     */
    public GameMap getLoadedMap() {
        return this.curMap;
    }
}
