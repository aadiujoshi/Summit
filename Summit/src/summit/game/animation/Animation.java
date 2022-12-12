package summit.game.animation;

import summit.gfx.Paintable;
import summit.util.ScheduledEvent;

public abstract class Animation extends ScheduledEvent implements Paintable{
    public Animation(long delay_ms, int n_calls) {
        super(delay_ms, n_calls);
    }
}
