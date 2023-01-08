package summit.game.animation;

import java.awt.Point;

import summit.gfx.ColorFilter;
import summit.gfx.Light;
import summit.gfx.OrderPaintEvent;
import summit.gfx.PaintEvent;
import summit.gfx.RenderLayers;
import summit.gfx.Renderer;
import summit.util.GameScheduler;
import summit.util.GraphicsScheduler;

/**
 * Represents an animation that creates a glistening effect on a specified tile.
 * 
 * This class extends Animation and is used to create an animation that consists
 * of a
 * series of small, flickering lights on a specified tile. The number of lights
 * and
 * their color can be specified when the animation is created. The animation is
 * rendered on the top layer of the game's render stack, ensuring that it is
 * drawn
 * on top of all other objects in the game.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R.
 */
public class GlistenAnimation extends Animation {

    /** The color of the lights in the animation. */
    private int color;

    /** An array that stores the number of times each light has been called. */
    private int[] calls;

    /** An array that stores the `Light` objects that make up the animation. */
    private Light[] lights;

    /** A `ColorFilter` that is applied to the lights in the animation. */
    private ColorFilter light_filter;

    // origin

    /** The x-coordinate of the tile on which the animation is displayed. */
    private float sx;

    /** The y-coordinate of the tile on which the animation is displayed. */
    private float sy;

    /** The spread of the lights in the animation. */
    private float spread = 0.75f;

    /** The length of a single frame of the animation, in milliseconds. */
    private static final int frame_len = (int) (Math.random() * 100 + 100);

    /**
     * Constructs a new `GlistenAnimation` with the specified number of lights and
     * color.
     * 
     * @param x       the x-coordinate of the tile on which the animation is
     *                displayed
     * @param y       the y-coordinate of the tile on which the animation is
     *                displayed
     * @param p_count the number of lights in the animation
     * @param color   the color of the lights in the animation
     */
    public GlistenAnimation(float x, float y, int p_count, int color) {
        super(frame_len, p_count + 1);
        
        this.sx = x + 0.5f;
        this.sy = y + 0.5f;

        this.light_filter = new ColorFilter(color);

        lights = new Light[p_count];
        calls = new int[p_count];

        for (int i = 0; i < lights.length; i++) {
            lights[i] = Light.NO_LIGHT;
        }

        for (int i = 0; i < calls.length; i++) {
            calls[i] = -i;
        }

        GraphicsScheduler.registerEvent(this);
    }

    /**
     * Adds the animation to the top layer of the game's render stack.
     * 
     * This method is called when the animation is registered with the
     * `GraphicsScheduler` and adds the animation and its lights to the top layer
     * of the game's render stack. This ensures that the lights are drawn on top
     * of all other objects in the game.
     * 
     * @param ope the `OrderPaintEvent` object that stores the render stack
     */
    @Override
    public void setRenderLayer(OrderPaintEvent ope) {
        super.setRenderLayer(ope);

        for (int i = 0; i < lights.length; i++) {
            lights[i].setRenderLayer(ope);
        }
    }

    /**
     * Draws the lights on the screen.
     * 
     * This method is called by the game's renderer and draws each light in the
     * `lights` array on the screen using the specified color.
     * 
     * @param e the `PaintEvent` object that stores the renderer and graphics
     *          context
     */
    @Override
    public void paint(PaintEvent e) {
        for (int i = 0; i < lights.length; i++) {
            Point po = Renderer.toPixel(lights[i].getX(), lights[i].getY(), e.getCamera());

            if (calls[i] == 0 || calls[i] == 2) {
                // System.out.println(calls[i]);
                // System.out.println(lights[i].getX() + lights[i].getY());
                e.getRenderer().fillRect(po.x, po.y, 1, 1, color);
            }
            if (calls[i] == 1) {
                e.getRenderer().fillRect(po.x - 1, po.y, 1, 1, color);
                e.getRenderer().fillRect(po.x, po.y - 1, 1, 1, color);
                e.getRenderer().fillRect(po.x + 1, po.y, 1, 1, color);
                e.getRenderer().fillRect(po.x, po.y + 1, 1, 1, color);
            }
        }
    }

    /**
     * Updates the animation and its lights.
     * 
     * This method is called by the `GameScheduler` at regular intervals and updates
     * the state of the animation and its lights. If a light has not yet been
     * activated, it is placed on a random location on the specified tile. If a
     * light
     * has been activated, its
     * calls variable is incremented. If the calls variable reaches a certain
     * value,
     * the light is deactivated and removed from the game's render stack.
     */
    @Override
    public void run() {
        for (int i = 0; i < lights.length; i++) {
            if (calls[i] == -1) {
                float nx = sx + (float) (Math.random() * spread - spread);
                float ny = sy + (float) (Math.random() * spread - spread);

                lights[i] = new Light(nx, ny, 0.25f, light_filter);
                lights[i].setRenderLayer(getRLayer());
            }   
            if (calls[i] != 0 && calls[i] != 1 && calls[i] != -1) {
                lights[i] = Light.NO_LIGHT;
            }

            calls[i]++;
        }
    }
}
