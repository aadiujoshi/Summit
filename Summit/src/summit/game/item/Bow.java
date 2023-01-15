package summit.game.item;

import summit.game.GameUpdateEvent;
import summit.game.entity.Entity;
import summit.game.entity.projectile.Arrow;
import summit.game.entity.projectile.Projectile;
import summit.gfx.Sprite;
import summit.util.Region;

/**
 *
 * 
 * The Bow class represents a bow in the game. It is a subclass of the
 * {@link ProjectileWeapon} class and represents a weapon that fires arrows.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R. S.
 */
public class Bow extends ProjectileWeapon {

    /**
     *
     * 
     * Creates a new instance of the Bow class and sets the owner of the weapon
     * to the specified entities.
     * 
     * @param owner The entities that owns this weapon
     */
    public Bow(Entity owner) {
        super(owner);
        super.setSprite(Sprite.BOW);
        super.setBaseDamage(1);
        super.setTextName("bow");
    }

    /**
     *
     * 
     * Uses the bow weapon to fire an arrow towards the specified target
     * coordinates.
     * It will only have an effect if the player has an arrow in their inventory.
     * 
     * @param targetX x-coordinate of the target
     * @param targetY y-coordinate of the target
     * @param e       The GameUpdateEvent that triggered this method call
     * @return true if the bow was successfully used, false otherwise
     */
    @Override
    public boolean useWeapon(float targetX, float targetY, GameUpdateEvent e) {
        if (getOwner().countItems(Sprite.ARROW_ITEM) > 0)
            return super.useWeapon(targetX, targetY, e);
        return false;
    }

    /**
     *
     * 
     * Creates a new instance of an arrow projectile, with the specified target
     * coordinates and the damage calculated by this weapon.
     * 
     * @param tx x-coordinate of the target
     * @param ty y-coordinate of the target
     * @return a new Arrow instance
     */
    @Override
    public Projectile getProjInstance(float tx, float ty) {
        return new Arrow(getOwner(),
                Region.theta(tx, getOwner().getX(),
                        ty, getOwner().getY()),
                getNetDamage());
    }

    /**
     *
     * 
     * Uses one arrow item from the player's inventory
     */
    @Override
    public void use() {
        getOwner().useItem(Sprite.ARROW_ITEM);
    }

    /**
     *
     * 
     * Creates a copy of the current bow item.
     * 
     * @return A copy of the current bow item.
     */
    @Override
    public Item copy() {
        return new Bow(getOwner());
    }
}
