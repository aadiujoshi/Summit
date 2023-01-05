package summit.game.animation;

import summit.gfx.Paintable;
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
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R.
 */
public abstract class Animation extends ScheduledEvent implements Paintable {

    /**
     * 
     * Constructs a new Animation.
     * 
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
}
