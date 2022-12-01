/*
* BPA project by Aadi Joshi, Aditya Bhattaharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.ai;

import java.io.Serializable;

import summit.game.GameUpdateEvent;
import summit.game.entity.Entity;

public abstract class EntityAI implements Serializable{

    protected Entity entity;

    public EntityAI(Entity e){
        this.entity = e;
    }

    public abstract void next(GameUpdateEvent e);
}
