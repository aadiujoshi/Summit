package summit.game.item;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import summit.game.GameUpdateEvent;
import summit.game.entity.Entity;
import summit.game.entity.projectile.Projectile;
import summit.util.GameObject;
import summit.util.Region;

public abstract class ProjectileWeapon extends WeaponItem{
    
    //used to create projectiles determined by stubclasses
    private transient Constructor<? extends Projectile> projType;

    public ProjectileWeapon(Entity owner) {
        super(owner);
        super.setAttackRange(5f);
    }

    @Override
    public boolean useWeapon(float targetX, float targetY, GameUpdateEvent e) {
        Entity owner = getOwner();

        this.use();
        
        try {
            Projectile p = projType.newInstance(
                            owner,
                            Region.theta(targetX, owner.getX(), 
                                        targetY, owner.getY()), 
                            getNetDamage());
                        
            e.getMap().spawn(p);

            return true;
        } catch (InstantiationException | 
                IllegalAccessException | 
                IllegalArgumentException | 
                InvocationTargetException | 
                SecurityException ex) {

            //this will never happen
            ex.printStackTrace();
        }

        return false;
    }
    
    public void setProjType(Class<? extends Projectile> projClass) {
        try {
            this.projType = projClass.getConstructor(GameObject.class, float.class, float.class);
        } catch (NoSuchMethodException | 
                SecurityException e) {
            e.printStackTrace();
        }
    }
}
