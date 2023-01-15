/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.animation;

import summit.gfx.OrderPaintEvent;
import summit.gfx.PaintEvent;
import summit.gfx.RenderLayers;
import summit.gfx.Renderer;
import summit.util.ScheduledEvent;
import summit.util.GameScheduler;
import summit.util.GraphicsScheduler;
import summit.util.Time;

/**
 * 
 * Represents a foreground animation that moves particles in a specified
 * direction.
 * 
 * This class extends Animation and is used to create an animation that consists
 * of moving particles in a specified
 * 
 * direction. The particles are drawn on top of all other objects in the game
 * and are randomly positioned on the screen
 * 
 * when the animation is created. The direction and velocity of the particles
 * can be specified when the animation is
 * 
 * created. The color of the particles can also be specified.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R. S.
 */
public class ForegroundAnimation extends Animation {

    /**
     * The x-velocity of the particles. Positive values move the particles to the
     * right.
     */
    private int dx;

    /* The y-velocity of the particles. Positive values move the particles down. */
    private int dy;

    /** The color of the particles. */
    private int pColor;

    
    /**
     * 
     * The positions of the particles. The x and y positions of each particle are
     * stored as 16-bit integers in a
     * single long value, with the x position stored in the first 16 bits
     * and the y position stored in the last 16 bits.
     */
    private long[] particles;

    /**
     * 
     * Constructs a new ForegroundAnimation with the specified particle velocities
     * and color.
     * 
     * @param dx     the x-velocity of the particles. Positive values move the
     *               particles to the right.
     * 
     * @param dy     the y-velocity of the particles. Positive values move the
     *               particles down.
     * 
     * @param pColor the color of the particles
     */
    public ForegroundAnimation(int dx, int dy, int pColor) {
        super(Time.MS_IN_S / 100, ScheduledEvent.FOREVER);
        super.setRLayer(RenderLayers.TOP_LAYER);
        
        this.dx = dx;
        this.dy = dy;

        this.pColor = pColor;

        particles = new long[200];

        for (int i = 0; i < particles.length; i++) {
            particles[i] = (((int) (Math.random() * Renderer.WIDTH)) << 16) |
                    ((int) (Math.random() * Renderer.HEIGHT) << 0);
        }

        GraphicsScheduler.registerEvent(this);
    }

    /**
     * Updates the position of the particles in the animation.
     * 
     * This method iterates through the `particles` array and adds the `dx` and `dy`
     * values
     * (which represent the x and y velocities of the particles) to the x and y
     * positions
     * of each particle. It also adds a random offset to the position of each
     * particle to
     * give the animation a more dynamic and random appearance. Finally, it updates
     * the
     * position of each particle in the `particles` array with the new x and y
     * positions.
     */
    @Override
    public void run() {
        for (int i = 0; i < particles.length; i++) {

            // random offset
            int cx = Math.random() > 0.5 ? 1 : -1;
            int cy = Math.random() > 0.5 ? 1 : -1;

            int nx = (int) ((particles[i] >> 16) & 0xff) + dx + cx;
            int ny = (int) ((particles[i] >> 0) & 0xff) + dy + cy;

            particles[i] = (nx << 16) | (ny << 0);
        }
    }
    
    /**
     * Draws the particles on the screen.
     * 
     * This method is called by the game's renderer and draws each particle in the
     * `particles` array on the screen using the specified color.
     * 
     * @param e the `PaintEvent` object that stores the renderer and graphics
     *          context
     */
    @Override
    public void paint(PaintEvent e) {
        for (int i = 0; i < particles.length; i++) {
            e.getRenderer().fillRect((int) ((particles[i] >> 16) & 0xff),
                    (int) ((particles[i] >> 0) & 0xff),
                    1, 1, pColor);
        }
    }
}
