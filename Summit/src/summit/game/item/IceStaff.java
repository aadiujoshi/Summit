package summit.game.item;

import summit.game.entity.Entity;
import summit.game.entity.projectile.IceShard;
import summit.game.entity.projectile.Projectile;
import summit.gfx.ColorFilter;
import summit.gfx.Light;
import summit.gfx.PaintEvent;
import summit.gfx.Sprite;
import summit.util.Region;

/**
 *
 * 
 * The IceStaff class represents an ice staff weapon in the game. It is a
 * subclass of
 * 
 * the {@link ProjectileWeapon} class and represents a weapon that can be used
 * to shoot ice shards.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R.
 */
public class IceStaff extends ProjectileWeapon {

    /**
     *
     * 
     * Creates a new instance of the IceStaff class and sets the owner of the weapon
     * to the specified Entity.
     * 
     * @param owner The entities that owns this weapon
     */
    public IceStaff(Entity owner) {
        super(owner);
        super.setSprite(Sprite.ICE_STAFF);
        super.setBaseDamage(3.5f);
        super.setTextName("ice staff");
    }

    /**
     *
     * 
     * Creates a new instance of the IceShard class and sets its direction, damage
     * and owner.
     * 
     * @param tx the x-coordinate of the target
     * @param ty the y-coordinate of the target
     * @return the instance of IceShard created
     */
    @Override
    public Projectile getProjInstance(float tx, float ty) {
        return new IceShard(getOwner(),
                Region.theta(tx, getOwner().getX(),
                        ty, getOwner().getY()),
                getNetDamage());
    }

    /**
     *
     * 
     * Uses the IceStaff item to fire an {@link IceShard} projectile. It does not
     * consume any resources.
     */
    @Override
    public void use() {
        // do nothing
    }

    /**
     * 
     * Creates a copy of the current IceStaff item.
     * 
     * @return A copy of the current IceStaff item.
     */
    @Override
    public Item copy() {
        return new IceStaff(getOwner());
    }

    /**
     * 
     * Renders a light effect on the IceStaff when it's not being used in water.
     * 
     * @param e the PaintEvent to be used for rendering the light effect
     */
    @Override
    public void paint(PaintEvent e) {
        super.paint(e);
        if (!getOwner().is(Entity.inWater))
            e.getRenderer().renderLight(new Light(
                    super.getX() + 0.9f,
                    super.getY() + 0.5f,
                    1, new ColorFilter(0, 40, 120)), e.getCamera());
    }
}
