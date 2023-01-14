package summit.game.item;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import summit.game.GameUpdateEvent;
import summit.game.entity.Entity;
import summit.game.entity.projectile.Projectile;
import summit.gfx.Camera;
import summit.gfx.ColorFilter;
import summit.gfx.OrderPaintEvent;
import summit.gfx.PaintEvent;
import summit.gfx.Paintable;
import summit.gfx.RenderLayers;
import summit.gfx.Renderer;
import summit.util.GameObject;
import summit.util.Region;

/**
 * 
 * Represents a WeaponItem, which is an item that can be used to attack other
 * entities.
 * 
 * It extends the Item class and implements the Paintable interface.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R.
 */
public abstract class WeaponItem extends Item implements Paintable {

    private float attackRange;

    private float mult;
    private float baseDamage;

    private float x;
    private float y;

    /**
     * 
     * Constructs a new WeaponItem.
     * 
     * @param owner the Entity that owns this weapon
     */
    public WeaponItem(Entity owner) {
        super(owner);

        // ATTACK_TYPE = type;
        baseDamage = 1;
        mult = 1;
    }

    
    /** 
     * @param addLevel(
     */
    /**
     * 
     * Uses the weapon. The behavior of this method is determined by the subclasses.
     */
    @Override
    public abstract void use();

    /**
     * 
     * Increases the level of the weapon.
     */
    public void addLevel() {
        this.mult += 0.2f;
    }

    /**
     * 
     * Gets the net damage of the weapon.
     * 
     * @return the net damage of the weapon
     */
    public float getNetDamage() {
        return this.baseDamage * mult;
    }

    /**
     * 
     * Sets the base damage of the weapon.
     * 
     * @param baseDamage the base damage of the weapon
     */
    public void setBaseDamage(float baseDamage) {
        this.baseDamage = baseDamage;
    }

    
    /** 
     * @param targetX
     * @param targetY
     * @param e
     * @return boolean
     */
    /**
     * 
     * Uses the weapon to attack a target.
     * 
     * @param targetX the x coordinate of the target
     * @param targetY the y coordinate of the target
     * @param e       the game update event
     * @return true if the weapon hit a target, false otherwise
     */
    public abstract boolean useWeapon(float targetX, float targetY, GameUpdateEvent e);

    /**
     * 
     * Sets the render layer for the weapon.
     * 
     * @param e the order paint event
     */
    @Override
    public void setRenderLayer(OrderPaintEvent e) {
        e.addToLayer(RenderLayers.STRUCTURE_ENTITY_LAYER, this);
    }

    /**
     * 
     * Paints the weapon on the screen.
     * 
     * @param e the paint event
     */
    @Override
    public void paint(PaintEvent e) {
        Entity owner = getOwner();

        if (owner.is(Entity.inWater))
            return;

        x = owner.getX();
        y = owner.getY();

        // prevent stuttering
        if (owner.getName().equals("Player")) {
            x = e.getCamera().getX();
            y = e.getCamera().getY();
        }

        this.x += owner.getSpriteOffsetX();
        this.y += owner.getSpriteOffsetY();

        e.getRenderer().renderGame(getSprite(), x + 0.5f, y - 0.2f,
                (getOwner().getRenderOp() & ~Renderer.FLIP_X),
                ColorFilter.NOFILTER,
                e.getCamera());
    }

    /**
     * Returns the x-coordinate of the weapon's location.
     * 
     * @return float The x-coordinate of the weapon's location.
     */
    public float getX() {
        return this.x;
    }

    /**
     * Returns the y-coordinate of the weapon's location.
     * 
     * @return float The y-coordinate of the weapon's location.
     */
    public float getY() {
        return this.y;
    }

    /**
     * Returns the range of the weapon's attack.
     * 
     * @return float The range of the weapon's attack.
     */
    public float getAttackRange() {
        return this.attackRange;
    }

    /**
     * Sets the range of the weapon's attack.
     * 
     * @param attackRange The new range of the weapon's attack.
     */
    public void setAttackRange(float attackRange) {
        this.attackRange = attackRange;
    }
}
