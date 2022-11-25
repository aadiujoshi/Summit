package summit.game.animation;

import summit.util.Time;

public abstract class ScheduledEvent implements Runnable {
    
    public final static int FOREVER = -1;

    private int repeat;
    private long delay_ms;
    private long lastCall;
    private final long INIT_MS;

    public ScheduledEvent(long delay_ms, int n_repeat){
        this.delay_ms = delay_ms;
        this.repeat = n_repeat;
        this.INIT_MS = Time.timeMs();
        this.lastCall = INIT_MS;
    }

    /**
     * Returns if this ScheduledEvent should stop revieving calls
     * @return boolean
     */
    public boolean terminate(){
        return repeat < 1 && repeat != FOREVER;
    }

    public void shortenLife(){
        if(repeat != FOREVER)
            repeat--;
    }
    
    public long getDelayMS() {
        return this.delay_ms;
    }
    
    public void setDelayMS(long delay_ms) {
        this.delay_ms = delay_ms;
    }

    public long getInitTime(){
        return INIT_MS;
    }
    
    public long getLastCall() {
        return this.lastCall;
    }

    public void setLastCall(long lastCall) {
        this.lastCall = lastCall;
    }
}
