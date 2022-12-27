package summit.game.item;

import java.util.ArrayList;

import summit.game.GameUpdateEvent;
import summit.game.entity.Entity;
import summit.gfx.Camera;
import summit.util.GameRegion;
import summit.util.Region;

public abstract class MeleeWeapon extends WeaponItem{

    public MeleeWeapon(Entity owner) {
        super(owner);
    }

    @Override
    public void use() {
    }
    
    @Override
    public void useWeapon(float targetX, float targetY, GameUpdateEvent e) {
        Entity owner = getOwner();

        this.use();

        if(Region.distance(owner.getX(), 
                                owner.getY(), 
                                targetX, 
                                targetY) <= owner.getAttackRange()){

            ArrayList<GameRegion> toHit = e.getMap().objectsInDist(new Camera(owner.getX(), owner.getY()), targetY);
            
            //check relative angle
            for (GameRegion gr : toHit) {

                float angle = (float)(Region.theta(owner.getX(), gr.getX(),
                                                    owner.getY(), gr.getY()) 
                                                    *180/Math.PI);

            }
        }
    }
}
