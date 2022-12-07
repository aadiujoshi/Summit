/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.util;

import java.io.Serializable;

public abstract class ScheduledEvent implements Serializable, Runnable {
    
    public final static int FOREVER = -1;

    private int n_calls;
    private long delay_ms;
    private long lastCall;
    private final long INIT_MS;

    public ScheduledEvent(long delay_ms, int n_calls){
        this.delay_ms = delay_ms;
        this.n_calls = n_calls;
        this.INIT_MS = Time.timeMs();
        this.lastCall = INIT_MS;
    }

    public void reinit(){
        Scheduler.registerEvent(this);
    }

    /**
     * Returns if this ScheduledEvent should stop revieving calls
     * @return boolean
     */
    public boolean terminate(){
        return n_calls < 1 && n_calls != FOREVER;
    }

    public void shortenLife(){
        if(n_calls != FOREVER)
            n_calls--;
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
