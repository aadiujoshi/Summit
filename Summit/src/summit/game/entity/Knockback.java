/*
* BPA project by Aadi Joshi, Aditya Bhattaharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.entity;

import java.io.Serializable;

import summit.game.GameMap;
import summit.game.GameUpdateEvent;
import summit.util.Region;
import summit.util.Time;

public class Knockback implements Serializable {

    private float sx;
    private float sy;

    private float kx;
    private float ky;

    private float ax;
    private float ay;

    private final long START_TIME = Time.timeNs();

    private int duration_ms;

    private Entity hitEntity;

    public Knockback(float k, Entity hitEntity, Entity hitBy, int duration_ms){

        this.sx = hitEntity.getX();
        this.sy = hitEntity.getY();

        this.duration_ms = duration_ms;

        this.hitEntity = hitEntity;
        
        //find angle
        double theta = Region.theta(hitBy.getX(), sx, hitBy.getY(), sy);

        float nkx = k * (float)Math.cos(theta);
        float nky = k * (float)Math.sin(theta);

        this.kx = -nkx;
        this.ky = -nky;

        this.ax = -kx / ((float)duration_ms/Time.MS_IN_S);
        this.ay = -ky / ((float)duration_ms/Time.MS_IN_S);
    }

    public boolean finished(){
        return Time.timeNs() - START_TIME >= duration_ms*Time.NS_IN_MS;
    }
    
    public void move(GameUpdateEvent e){
        GameMap map = e.getMap();

        float delta_t = (Time.timeNs() - START_TIME)/(float)Time.NS_IN_S;
        
        float nx = sx + (kx * delta_t) + (ax/2) * (delta_t*delta_t);
        float ny = sy + (ky * delta_t) + (ay/2) * (delta_t*delta_t);

        if(hitEntity.moveTo(map, nx, ny)){
            hitEntity.setPos(nx, ny);
        }
    }
}
