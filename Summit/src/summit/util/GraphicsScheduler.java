/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.util;

import java.util.ArrayList;

/**
 * This scheduler is used for animations, and visuals. Used for handling ScheduledEvents and giving them updates
 */
public class GraphicsScheduler {
    
    private static ArrayList<ScheduledEvent> events = new ArrayList<>();

    private static ArrayList<ScheduledEvent> eventAddQueue = new ArrayList<>();

    /** Singleton, no instantiation */
    private GraphicsScheduler(){}

    //called by some thread
    public static void checkEvents(){
        
        for(int i = 0; i < events.size(); i++) {
            long now = Time.timeMs();
        
            ScheduledEvent e = events.get(i);
            
            if(e == null){
                events.remove(i);
                i--;
                continue;
            }
            if(now - e.getLastCall() >= e.getDelayMS()){
                e.run();
                e.shortenLife();
                e.setLastCall(now);
            }
            if(e.shouldTerminate()){
                //call delayed calls
                while(e.callsLeft() > 0){
                    e.run();
                    e.shortenLife();
                }

                events.remove(i);
                i--;
            }
        }

        synchronized(eventAddQueue){
            for (ScheduledEvent se : eventAddQueue) {
                events.add(se);
            }
            eventAddQueue.clear();
        }
    }
    
    /** 
     * @param e The {@code ScheduledEvent} to add to this scheduler
     */
    public static void registerEvent(ScheduledEvent e){
        synchronized(eventAddQueue){
            eventAddQueue.add(e);
        }
    }
}
