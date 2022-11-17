package summit.game.animation;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import summit.util.Time;

public class Scheduler {

    private static List<ScheduledEvent> events = new Vector<>();

    /** Singleton */
    private Scheduler(){}

    //called by some thread
    public static void checkEvents(){
        long now = Time.timeMs();

        for(int i = 0; i < events.size(); i++) {
            ScheduledEvent e = events.get(i);
            if(now - e.getLastCall() >= e.getDelayMS()){
                e.run();
                e.shortenLife();
                e.setLastCall(now);
            }
            if(e.terminate()){
                events.remove(i);
                i--;
            }
        }
    }

    public static void registerEvent(ScheduledEvent e){
        events.add(e);
    }
}
