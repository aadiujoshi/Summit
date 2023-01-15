package summit.game.animation;

import java.awt.Point;

import summit.gfx.OrderPaintEvent;
import summit.gfx.PaintEvent;
import summit.gfx.RenderLayers;
import summit.gfx.Renderer;
import summit.util.GameScheduler;
import summit.util.GraphicsScheduler;
import summit.util.Time;

/**
 * Represents an animation that consists of moving particles.
 *
 * This class extends the `Animation` class and is used to create an animation
 * that consists of
 * moving particles. The particles are randomly positioned on the screen when
 * the animation is
 * created and the velocity of the particles can be specified. The color of the
 * particles can also
 * be specified. The particles are affected by gravity and move in a specified
 * direction.
 *
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R. S.
 */
public class ParticleAnimation extends Animation {

    /** The color of the particles. */
    private int color;

    /**
     * The x-velocities of the particles. Positive values move the particles to the
     * right.
     * Negative values move the particles to the left.
     */
    private float[] pdx;
    /**
     * The y-velocities of the particles. Positive values move the particles down.
     * Negative values move the particles up.
     */
    private float[] pdy;

    /** The x-positions of the particles. */
    private float[] px;
    /** The y-positions of the particles. */
    private float[] py;

    /** The number of particles in the animation. */
    private int pCount;

    /**
     * The acceleration due to gravity applied to the particles, in units of game
     * tiles per second per second.
     */
    private static final float gravity = -5;

    /** The x-position of the origin of the animation, in game tiles. */
    private float sx;
    /** The y-position of the origin of the animation, in game tiles. */
    private float sy;

    /**
     * Constructs a new `ParticleAnimation` with the specified duration, number of
     * particles, and color.
     *
     * @param x             the x-position of the origin of the animation, in game
     *                      tiles
     * @param y             the y-position of the origin of the animation, in game
     *                      tiles
     * @param duration_ms   the duration of the animation, in milliseconds
     * @param particleCount the number of particles in the animation
     * @param color         the color of the particles
     */
    public ParticleAnimation(float x, float y, int duration_ms, int particleCount, int color) {
        super(1, duration_ms);
        super.setRLayer(RenderLayers.TILE_LAYER+1);

        pdx = new float[particleCount];
        pdy = new float[particleCount];
        px = new float[particleCount];
        py = new float[particleCount];

        this.sx = x;
        this.sy = y;
        this.pCount = particleCount;
        this.color = color;

        for (int i = 0; i < particleCount; i++) {
            pdx[i] = (float) Math.random() * 3 - 1.5f;
            pdy[i] = (float) Math.random() * 3;
            px[i] = sx;
            py[i] = sy;
        }

        GraphicsScheduler.registerEvent(this);
    }

    /**
     * Renders this `ParticleAnimation` on the game screen.
     *
     * This method draws a single pixel at the position of each particle in this
     * `ParticleAnimation`.
     * If this `ParticleAnimation` has finished running, this method does not do
     * anything.
     *
     * @param e the `PaintEvent` used to render this `ParticleAnimation`
     */
    @Override
    public void paint(PaintEvent e) {
        if (shouldTerminate())
            return;

        for (int i = 0; i < pCount; i++) {
            Point p = Renderer.toPixel(px[i], py[i], e.getCamera());
            e.getRenderer().fillRect(p.x, p.y, 1, 1, color);
        }
    }

    /**
     * Updates the position of each particle in this `ParticleAnimation` based on
     * its velocity and the elapsed time.
     *
     * This method calculates the new position of each particle in this
     * `ParticleAnimation` based on its initial velocity,
     * the elapsed time since the start of this `ParticleAnimation`, and a constant
     * gravitational force.
     */
    @Override
    public void run() {

        // time elapsed in seconds
        float delta_t = (Time.timeMs() - getInitTime()) / 1000f;

        for (int i = 0; i < pCount; i++) {

            // delta pos from velocities
            float ndx = pdx[i] * delta_t;
            float ndy = pdy[i] * delta_t;

            float nx = (sx + ndx);
            float ny = (sy + ndy + (gravity / 2) * (delta_t * delta_t));

            px[i] = nx;
            py[i] = ny;
        }
    }
}
