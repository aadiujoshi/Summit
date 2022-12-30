/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.ai;

import summit.game.GameUpdateEvent;
import summit.game.entity.mob.MobEntity;
import summit.game.entity.mob.Player;

public class HostileMobAI extends EntityAI{
    
    //range to start attacking player
    private float interestRange = 10;

    public HostileMobAI(MobEntity e){
        super(e);
    }

    @Override
    public void next(GameUpdateEvent e) {
        super.next(e);


        //attack range of the equipped weapon
        float attackRange = entity.getAttackRange();

        Player p = e.getMap().getPlayer();
        
        float dist = entity.distance(p);
        boolean los = entity.lineOfSight(p, e.getMap());

        // System.out.println(dist + "  " + e);

        if(dist > interestRange || !los) {
            super.wander(true);
            // System.out.println("WAndering");
            return;
        } else if(dist <= interestRange && dist > attackRange-1 && los){
            setDest(p.getX(), p.getY());
            super.wander(false);
            // System.out.println("going to attacking position");
        }

        if(dist <= attackRange-1){
            super.wander(true);
        //     // System.out.println("reached attack position");
        }

        if(super.reachedDest()){
            entity.attack(p.getX(), p.getY(), e);
            // System.out.println("reached attack position");
        }
    }
    
    @Override
    public void reinit() {
    
    }
}
