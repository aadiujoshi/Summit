package summit.game.item;

import summit.game.GameUpdateEvent;
import summit.game.entity.Entity;
import summit.game.entity.projectile.Projectile;

/**
 * 
 * The ProjectileWeapon class represents a type of weapon that fires
 * projectiles. It extends the WeaponItem class and
 * 
 * overrides the useWeapon(float targetX, float targetY, GameUpdateEvent e)
 * method, providing specific behavior for
 * 
 * this type of weapon. The class also sets the attack range to 5 units.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R.
 */
public abstract class ProjectileWeapon extends WeaponItem {

    // used to create projectiles determined by stubclasses
    // private transient Constructor<? extends Projectile> projType;

    /**
     * 
     * Creates a new instance of the ProjectileWeapon class and sets the owner of
     * the weapon to the specified entity.
     * 
     * @param owner The entity that owns the weapon
     */
    public ProjectileWeapon(Entity owner) {
        super(owner);
        super.setAttackRange(5f);
    }

    /**
     * 
     * Returns a new instance of a projectile to be fired. This method is to be
     * implemented by subclasses.
     * 
     * @param targetX The x-coordinate of the target location
     * @param targetY The y-coordinate of the target location
     * @return A new instance of a projectile to be fired
     */
    public abstract Projectile getProjInstance(float targetX, float targetY);

    /**
     * 
     * Allows the weapon to be used on a target at a specified location.
     * 
     * Overrides the useWeapon method in WeaponItem.
     * 
     * @param targetX The x-coordinate of the target location
     * 
     * @param targetY The y-coordinate of the target location
     * 
     * @param e       The GameUpdateEvent that is passed to the method
     * 
     * @return A boolean indicating if the weapon was used successfully
     */
    @Override
    public boolean useWeapon(float targetX, float targetY, GameUpdateEvent e) {
        Entity owner = getOwner();

        this.use();

        Projectile p = getProjInstance(targetX, targetY);

        e.getMap().spawn(p);

        return true;

        // try {

        // } catch (InstantiationException |
        // IllegalAccessException |
        // IllegalArgumentException |
        // InvocationTargetException |
        // SecurityException ex) {

        // //this will never happen
        // ex.printStackTrace();
        // }
    }

    // public void setProjType(Class<? extends Projectile> projClass) {
    // try {
    // this.projType = projClass.getConstructor(GameObject.class, float.class,
    // float.class);
    // } catch (NoSuchMethodException |
    // SecurityException e) {
    // e.printStackTrace();
    // }
    // }
}
