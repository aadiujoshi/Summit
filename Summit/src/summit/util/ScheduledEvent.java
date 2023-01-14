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

    private boolean manualTerminate;

    // can be reset after reloading a world
    private long init_ms;

    private boolean paused;

    public ScheduledEvent(long delay_ms, int n_calls){
        this.delay_ms = delay_ms;
        this.n_calls = n_calls;
        this.init_ms = Time.timeMs();
        this.lastCall = init_ms;
    }

    
    /** 
     * @param p
     */
    public void setPaused(boolean p){
        paused = p;
    }

    
    /** 
     * @return boolean
     */
    public boolean paused(){
        return paused;
    }

    
    /** 
     * @param schedulerThread
     */
    public void reinit(int schedulerThread){
        this.init_ms = Time.timeMs();
        this.lastCall = init_ms;

        if(schedulerThread == 1){
            GameScheduler.registerEvent(this);
        } else {
            GraphicsScheduler.registerEvent(this);
        }
    }
    
    public void manualTerminate(){
        this.manualTerminate = true;
    }

    /**
     * Returns if this ScheduledEvent should stop revieving calls, 
     * and should be removed from the Scheduler 
     * @return boolean
     */
    public boolean shouldTerminate(){
        return (n_calls < 1 && n_calls != FOREVER) || manualTerminate;
    }

    public void shortenLife(){
        if(n_calls != FOREVER && !paused)
            n_calls--;
    }
    
    
    /** 
     * @return long
     */
    public long getDelayMS() {
        return this.delay_ms;
    }
    
    
    /** 
     * @param delay_ms
     */
    public void setDelayMS(long delay_ms) {
        this.delay_ms = delay_ms;
    }

    
    /** 
     * @return long
     */
    public long getInitTime(){
        return init_ms;
    }
    
    
    /** 
     * @return long
     */
    public long getLastCall() {
        return this.lastCall;
    }

    
    /** 
     * @param lastCall
     */
    public void setLastCall(long lastCall) {
        this.lastCall = lastCall;
    }
}
