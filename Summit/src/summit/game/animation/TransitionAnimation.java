package summit.game.animation;

import summit.gfx.OrderPaintEvent;
import summit.gfx.PaintEvent;
import summit.gfx.RenderLayers;
import summit.gfx.Renderer;
import summit.util.Region;

/**
 * A transition animation that expands a circle of random colors from the center
 * of the screen.
 *
 * This class extends Animation and is used to create a transition animation
 * that consists of a circle of random colors
 * expanding from the center of the screen. The radius of the circle increases
 * with each frame until the animation is complete.
 *
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R.
 */
public class TransitionAnimation extends Animation {

    /**
     * 
     * The radius of the gradient displayed by this TransitionAnimation.
     * The radius determines which pixels on the screen should be colored by
     * the gradient. Pixels farther from the center of the screen than the
     * radius will not be colored.
     */
    private int radius;

    /**
     * 
     * Constructs a new TransitionAnimation.
     * 
     * This animation will display a random color gradient over the screen that
     * 
     * grows from the center of the screen to the edges. The animation will
     * 
     * run for the duration required for the gradient to fully cover the screen.
     */
    public TransitionAnimation() {
        super(4, (int) Region.distance(Renderer.WIDTH / 2, Renderer.HEIGHT / 2, 0, 0) + 5);

        radius = 0;
    }

    /**
     * Adds this `TransitionAnimation` to the top layer of the rendering stack.
     *
     * This method adds this `TransitionAnimation` to the top layer of the rendering
     * stack, ensuring that it is drawn on top
     * of all other objects in the game.
     *
     * @param e the `OrderPaintEvent` that contains the rendering stack to which
     *          this `TransitionAnimation` should be added
     */
    @Override
    public void setRenderLayer(OrderPaintEvent e) {
        e.addToLayer(RenderLayers.TOP_LAYER, this);
    }

    /**
     * Fills the pixels outside the current radius with random colors.
     *
     * This method fills the pixels outside the current radius with random colors.
     * The radius of the circle increases with
     * each frame until the animation is complete.
     *
     * @param e the `PaintEvent` that contains the frame that should be modified by
     *          this `TransitionAnimation`
     */
    @Override
    public void paint(PaintEvent e) {
        int[][] frame = e.getRenderer().getFrame();

        for (int r = 0; r < frame.length; r++) {
            for (int c = 0; c < frame[0].length; c++) {
                if (Region.distance(c, r, Renderer.WIDTH / 2, Renderer.HEIGHT / 2) >= radius) {
                    frame[r][c] = Renderer.toIntRGB((int) (Math.random() * 256),
                            (int) (Math.random() * 256),
                            (int) (Math.random() * 256));
                    frame[r][c] = 0;
                }
            }
        }
    }

    /**
     * 
     * Called when this TransitionAnimation is scheduled to run.
     * This method increments the radius of the gradient displayed by this
     * animation.
     * The radius is used to determine which pixels on the screen should be colored.
     */
    @Override
    public void run() {
        radius++;
    }
}
