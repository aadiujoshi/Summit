package summit.game.item;

import summit.game.GameUpdateEvent;
import summit.game.entity.Entity;
import summit.game.entity.projectile.Projectile;

public abstract class ProjectileWeapon extends WeaponItem{
    
    //used to create projectiles determined by stubclasses
    // private transient Constructor<? extends Projectile> projType;

    public ProjectileWeapon(Entity owner) {
        super(owner);
        super.setAttackRange(5f);
    }

    public abstract Projectile getProjInstance(float targetX, float targetY);

    @Override
    public boolean useWeapon(float targetX, float targetY, GameUpdateEvent e) {
        Entity owner = getOwner();

        this.use();
        
        Projectile p = getProjInstance(targetX, targetY);
                        
        e.getMap().spawn(p);

        return true;
            
        // try {
            
        // } catch (InstantiationException | 
        //         IllegalAccessException | 
        //         IllegalArgumentException | 
        //         InvocationTargetException | 
        //         SecurityException ex) {

        //     //this will never happen
        //     ex.printStackTrace();
        // }
    }
    
    // public void setProjType(Class<? extends Projectile> projClass) {
    //     try {
    //         this.projType = projClass.getConstructor(GameObject.class, float.class, float.class);
    //     } catch (NoSuchMethodException | 
    //             SecurityException e) {
    //         e.printStackTrace();
    //     }
    // }
}
