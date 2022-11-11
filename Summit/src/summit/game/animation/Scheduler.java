package summit.game.animation;

import java.util.ArrayList;
import java.util.List;

import summit.util.Time;

public class Scheduler {

    private static List<ScheduledEvent> events = new ArrayList<>();

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
}
