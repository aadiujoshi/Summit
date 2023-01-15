package summit.game.item;

import java.util.ArrayList;

import summit.game.GameUpdateEvent;
import summit.game.entity.Entity;
import summit.gfx.Camera;
import summit.util.GameObject;
import summit.util.Region;

/**
 *
 * 
 * The MeleeWeapon class represents a type of weapon that can be used in close
 * combat. It extends the WeaponItem class and
 * 
 * overrides the use() and useWeapon(float targetX, float targetY,
 * GameUpdateEvent e) methods, providing specific behavior for
 * 
 * this type of weapon. The class also sets the attack range to 2 units.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R. S.
 */
public abstract class MeleeWeapon extends WeaponItem {

    /**
     *
     * 
     * Creates a new instance of the MeleeWeapon class and sets the owner of the
     * weapon to the specified entity.
     * 
     * @param owner The entity that owns the weapon
     */
    public MeleeWeapon(Entity owner) {
        super(owner);
        super.setAttackRange(2f);
    }

    /**
     *
     * 
     * Defines the specific behavior of the weapon when used. To be implemented by
     * subclasses.
     */
    @Override
    public void use() {
    }

    /**
     *
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
     * @return A boolean indicating if the weapon hit a target
     */
    @Override
    public boolean useWeapon(float targetX, float targetY, GameUpdateEvent e) {
        Entity owner = getOwner();

        this.use();

        float sweepRange = 30;

        float relAngle = Region.theta(owner.getX(), targetX,
                owner.getY(), targetY)
                * (float) (180 / Math.PI);

        ArrayList<GameObject> inRange = e.getMap().objectsInDist(new Camera(owner.getX(), owner.getY()),
                getAttackRange());

        boolean hit = false;

        // check relative angle
        for (int i = 0; i < inRange.size(); i++) {

            if (inRange.get(i) == owner)
                continue;

            float angle = (float) (Region.theta(owner.getX(), inRange.get(i).getX(),
                    owner.getY(), inRange.get(i).getY())
                    * 180 / Math.PI);

            if (Math.abs(relAngle - angle) <= sweepRange) {
                if (inRange.get(i) instanceof Entity &&
                        !inRange.get(i).is(Entity.damageCooldown) &&
                        !owner.is(Entity.attackCooldown)) {

                    ((Entity) inRange.get(i)).damage(e, owner);
                    hit = true;
                }
            }
        }

        return hit;
    }
}
