package summit.game.animation;

import summit.util.Time;

public abstract class ScheduledEvent implements Runnable {
    
    private int repeat;
    private long delay_ms;
    private long lastCall;
    private final long INIT_MS = Time.timeMs();

    public ScheduledEvent(long delay_ms, int n_repeat){
        this.delay_ms = delay_ms;
        this.repeat = n_repeat;
        this.lastCall = INIT_MS;
    }

    public boolean terminate(){
        return repeat < 1;
    }

    public void shortenLife(){
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
