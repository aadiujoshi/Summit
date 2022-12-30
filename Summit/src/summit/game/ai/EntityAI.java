/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.ai;

import java.io.Serializable;

import summit.game.GameUpdateEvent;
import summit.game.entity.Entity;
import summit.util.GameScheduler;
import summit.util.ScheduledEvent;
import summit.util.Time;

/**
 * Controls the movement of the parent Entity
 */
public class EntityAI implements Serializable{

    protected Entity entity;
    
    private float destX;
    private float destY;

    private boolean reachedDest;

    private ScheduledEvent wandering;

    public EntityAI(Entity e){
        this.entity = e;
    }

    public void wander(boolean w){
        if(!w && wandering != null){
            //remove from scheduler
            this.wandering.manualTerminate();
            this.wandering = null;
        }

        //only start once
        if(w && wandering == null){
            destX = entity.getX() + (float)(Math.random()*10-5);
            destY = entity.getY() + (float)(Math.random()*10-5);

            //1 second
            final long S = Time.MS_IN_S;

            this.wandering = new ScheduledEvent((long)(Math.random()*(10*S) + (5*S)), ScheduledEvent.FOREVER) {
                @Override
                public void run() {
                    this.manualTerminate();

                    destX = entity.getX() + (float)(Math.random()*10-5);
                    destY = entity.getY() + (float)(Math.random()*10-5);

                    this.setDelayMS((long)(Math.random()*(10*S) + (5*S)));
                }
            };

            GameScheduler.registerEvent(wandering);
        }
    }
    
    public void next(GameUpdateEvent e){
        float delta_x = entity.getDx()/Time.NS_IN_S * e.getDeltaTimeNS();
        float delta_y = entity.getDy()/Time.NS_IN_S * e.getDeltaTimeNS();

        delta_x *= (destX < entity.getX()) ? -1 : 1; 
        delta_y *= (destY < entity.getY()) ? -1 : 1; 

        int r = 0;

        if(Math.abs(entity.getX() - destX) >= 0.5f && entity.moveTo(e.getMap(), entity.getX() + delta_x, entity.getY()))
            entity.setX(entity.getX() + delta_x);
        else if(Math.abs(entity.getX() - destX) <= 0.5f)
            r++;
        
        if(Math.abs(entity.getY() - destY) >= 0.5f && entity.moveTo(e.getMap(), entity.getX(), entity.getY() + delta_y))
            entity.setY(entity.getY() + delta_y);
        else if(Math.abs(entity.getY() - destY) <= 0.5f)
            r++;
        
        if(r == 2)
            this.reachedDest = true;
    }

    public void reinit(){

    }

    public boolean reachedDest() {
        return this.reachedDest;
    }
    
    public void setDest(float destX, float destY){
        this.destX = destX;
        this.destY = destY;
    }
}
