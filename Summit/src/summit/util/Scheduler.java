/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.util;

import java.util.ArrayList;

public class Scheduler {
    
    private static ArrayList<ScheduledEvent> events = new ArrayList<>();

    private static ArrayList<ScheduledEvent> eventAddQueue = new ArrayList<>();

    /** Singleton */
    private Scheduler(){}

    //called by some thread
    public static void checkEvents(){
        // Time.nanoDelay(Time.NS_IN_MS/10);

        long now = Time.timeMs();
        
        for(int i = 0; i < events.size(); i++) {
            ScheduledEvent e = events.get(i);
            
            if(e == null){
                events.remove(e);
                i--;
                continue;
            }
            if(now - e.getLastCall() >= e.getDelayMS()){
                e.run();
                e.shortenLife();
                e.setLastCall(now);
            }
            if(e.shouldTerminate()){
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

    
    public static void registerEvent(ScheduledEvent e){
        synchronized(eventAddQueue){
            eventAddQueue.add(e);
        }
    }
}
