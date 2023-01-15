package summit.game.animation;

import summit.gfx.OrderPaintEvent;
import summit.gfx.Paintable;
import summit.gfx.RenderLayers;
import summit.util.ScheduledEvent;

/**
 * 
 * Represents a timed event that updates and draws an animation.
 * This abstract class extends ScheduledEvent and implements the Paintable
 * interface. It can be used to create
 * animations that are updated and drawn at regular intervals. The update and
 * draw behavior of the animation is
 * specified by the subclass.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R. S.
 */
public abstract class Animation extends ScheduledEvent implements Paintable {

    private int rLayer = RenderLayers.STRUCTURE_ENTITY_LAYER;

    /**
     * Constructs a new Animation.
     * 
     * @param delay_ms the delay between each call of the run method, in
     *                 milliseconds
     * @param n_calls  the number of times the run method should be called. Use
     *                 ScheduledEvent.FOREVER to call the run method indefinitely.
     * 
     * @see ScheduledEvent
     */
    public Animation(long delay_ms, int n_calls) {
        super(delay_ms, n_calls);
    }

    
    /**
     * Adds the animation to the top layer of the game's render stack.
     * 
     * This method is called when the animation is registered with the
     * `GraphicsScheduler` and adds the animation to the top layer of the
     * game's render stack.
     */
    @Override
    public void setRenderLayer(OrderPaintEvent e){
        e.addToLayer(rLayer, this);
    }

    
    /** 
     * @return int
     */
    public int getRLayer() {
        return this.rLayer;
    }

    
    /** 
     * @param rLayer
     */
    public void setRLayer(int rLayer) {
		this.rLayer = rLayer;
	}
}
