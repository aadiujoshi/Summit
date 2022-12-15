/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.ai;

import summit.game.GameUpdateEvent;
import summit.game.entity.Entity;
import summit.game.entity.mob.MobEntity;
import summit.game.entity.mob.Player;
import summit.util.Time;

public class HostileMobAI extends EntityAI{

    private float attackRange;

    public HostileMobAI(MobEntity e){
        super(e);
    }

    @Override
    public void next(GameUpdateEvent e) {

        Player p = e.getMap().getPlayer();

        float dest_x = p.getX();
        float dest_y = p.getY();

        float delta_x = entity.getDx()/Time.NS_IN_S * e.getDeltaTimeNS();
        float delta_y = entity.getDy()/Time.NS_IN_S * e.getDeltaTimeNS();

        delta_x *= (dest_x < entity.getX()) ? -1 : 1; 
        delta_y *= (dest_y < entity.getY()) ? -1 : 1; 

        int reached = 0;

        if(Math.abs(entity.getX() - dest_x) >= attackRange && entity.moveTo(e.getMap(), entity.getX() + delta_x, entity.getY()))
            entity.setX(entity.getX() + delta_x);
        else if(Math.abs(entity.getX() - dest_x) <= 0.5f)
            reached++;
        
        if(Math.abs(entity.getY() - dest_y) >= attackRange && entity.moveTo(e.getMap(), entity.getX(), entity.getY() + delta_y))
            entity.setY(entity.getY() + delta_y);
        else if(Math.abs(entity.getY() - dest_y) <= 0.5f)
            reached++;
        
        entity.attack(p);
    }
    
    public float getAttackRange() {
        return this.attackRange;
    }

    public void setAttackRange(float attackRange) {
        this.attackRange = attackRange;
    }
}
