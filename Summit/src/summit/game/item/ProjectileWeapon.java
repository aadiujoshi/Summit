package summit.game.item;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import summit.game.GameUpdateEvent;
import summit.game.entity.Entity;
import summit.game.entity.projectile.Projectile;
import summit.util.GameRegion;
import summit.util.Region;

public abstract class ProjectileWeapon extends WeaponItem{
    
    private transient Constructor<? extends Projectile> projType;

    public ProjectileWeapon(Entity owner) {
        super(owner);
    }

    @Override
    public void useWeapon(float targetX, float targetY, GameUpdateEvent e) {
        Entity owner = getOwner();

        this.use();
        

        try {
            Projectile p = projType.newInstance(
                            owner,
                            Region.theta(targetX, owner.getX(), 
                                        targetY, owner.getY()), 
                            getNetDamage());
                        
            e.getMap().spawn(p);

        } catch (InstantiationException | 
                IllegalAccessException | 
                IllegalArgumentException | 
                InvocationTargetException | 
                SecurityException ex) {

            //this will never happen
            ex.printStackTrace();
        }
    }
    
    public void setProjType(Class<? extends Projectile> projClass) {
        try {
            this.projType = projClass.getConstructor(GameRegion.class, float.class, float.class);
        } catch (NoSuchMethodException | 
                SecurityException e) {
            e.printStackTrace();
        }
    }
}
