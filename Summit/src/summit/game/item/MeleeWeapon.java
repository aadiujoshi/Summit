package summit.game.item;

import java.util.ArrayList;

import summit.game.GameUpdateEvent;
import summit.game.entity.Entity;
import summit.gfx.Camera;
import summit.util.GameObject;
import summit.util.Region;

public abstract class MeleeWeapon extends WeaponItem{
    
    public MeleeWeapon(Entity owner) {
        super(owner);
        super.setAttackRange(2f);
    }

    @Override
    public void use() {
    }
    
    @Override
    public boolean useWeapon(float targetX, float targetY, GameUpdateEvent e) {
        Entity owner = getOwner();

        this.use();

        float sweepRange = 30;

        float relAngle = Region.theta(owner.getX(), targetX, 
                                    owner.getY(), targetY) 
                                    * (float)(180/Math.PI);

        ArrayList<GameObject> inRange = e.getMap().
                    objectsInDist(new Camera(owner.getX(), owner.getY()), getAttackRange());
        
        boolean hit = false;
        
        //check relative angle
        for (int i = 0; i < inRange.size(); i++) {

            if(inRange.get(i) == owner)
                continue;

            float angle = (float)(Region.theta(owner.getX(), inRange.get(i).getX(),
                                                owner.getY(), inRange.get(i).getY()) 
                                                *180/Math.PI);
            
            if(Math.abs(relAngle - angle) <= sweepRange){
                if(inRange.get(i) instanceof Entity && 
                    !inRange.get(i).is(Entity.damageCooldown) &&
                    !owner.is(Entity.attackCooldown)){

                    ((Entity)inRange.get(i)).damage(e, owner);
                    hit = true;
                }
            }
        }
        
        return hit;
    }
}
