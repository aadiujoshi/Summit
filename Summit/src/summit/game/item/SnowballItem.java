package summit.game.item;

import summit.game.GameUpdateEvent;
import summit.game.entity.Entity;
import summit.game.entity.projectile.Projectile;
import summit.game.entity.projectile.Snowball;
import summit.gfx.Sprite;
import summit.util.Region;

/**
 * 
 * The SnowballItem class represents a type of weapon that fires snowballs. It
 * extends the ProjectileWeapon class and
 * 
 * overrides the useWeapon(float targetX, float targetY, GameUpdateEvent e)
 * method, providing specific behavior for
 * 
 * this type of weapon. The class also sets the sprite and text name of the
 * weapon.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R.
 */
public class SnowballItem extends ProjectileWeapon {

    /**
     * 
     * Creates a new instance of the SnowballItem class and sets the owner of the
     * weapon to the specified entity.
     * Also sets the sprite, text name, and base damage of the weapon.
     * 
     * @param owner The entity that owns the weapon
     */
    public SnowballItem(Entity owner) {
        super(owner);
        super.setSprite(Sprite.SNOWBALL);
        super.setBaseDamage(0);
        super.setTextName("snowballs");
    }

    /**
     * Allows the weapon to be used on a target at a specified location.
     * This method checks if the owner has any snowballs in their inventory before
     * using the weapon.
     * If the owner does have snowballs, it calls the superclass's useWeapon method,
     * updates the owner's equipped weapon,
     * and returns the value returned by the superclass's useWeapon method.
     * If the owner does not have snowballs, the method returns false.
     *
     * @param targetX The x-coordinate of the target location
     * @param targetY The y-coordinate of the target location
     * @param e       The game update event
     * @return true if the weapon was successfully used, false otherwise.
     */
    @Override
    public boolean useWeapon(float targetX, float targetY, GameUpdateEvent e) {
        boolean b = false;
        if (getOwner().countItems(Sprite.SNOWBALL) > 0) {
            getOwner().useItem(Sprite.SNOWBALL);
            b = super.useWeapon(targetX, targetY, e);

            var sLeft = getOwner().getItems().get(Sprite.SNOWBALL);

            if (sLeft.isEmpty())
                getOwner().setEquipped(null);
            else {
                getOwner().setEquipped((WeaponItem) sLeft.peek());
            }
        }

        return b;
    }

    /**
     * Returns a new instance of the Snowball projectile type with the specified
     * parameters.
     * 
     * @param tx The target x-coordinate
     * @param ty The target y-coordinate
     * @return A new instance of the Snowball projectile type
     */
    @Override
    public Projectile getProjInstance(float tx, float ty) {
        return new Snowball(getOwner(),
                Region.theta(tx, getOwner().getX(),
                        ty, getOwner().getY()),
                getNetDamage());
    }

    /**
     * Uses the SnowballItem. This method sets the used property of the item to
     * true.
     *
     */
    @Override
    public void use() {
        super.setUsed(true);
    }

    /**
     * Creates a new instance of the SnowballItem class with the same owner as the
     * current instance.
     * 
     * @return A new instance of the SnowballItem class with the same owner as the
     *         current instance.
     */
    @Override
    public Item copy() {
        return new SnowballItem(getOwner());
    }
}
