/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.entity.mob;

import java.util.HashMap;

import summit.game.GameUpdateEvent;
import summit.game.ai.EntityAI;
import summit.game.entity.Entity;
import summit.game.entity.projectile.Projectile;
import summit.game.item.BlueKey;
import summit.game.item.GreenKey;
import summit.game.item.RedKey;
import summit.game.item.WeaponItem;
import summit.gfx.ColorFilter;
import summit.gfx.Light;
import summit.gfx.PaintEvent;
import summit.gfx.Renderer;
import summit.util.Direction;

/**
 * 
 * MobEntity is a class that represents a mobile entity in the game. It extends
 * Entity and has additional attributes and methods for an AI, attack and damage
 * cooldowns, keys and a pickupItems flag. It also
 * has a paint method that changes the color filter of the MobEntity if it is in
 * the DungeonsMap and has no light.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R.
 */
public abstract class MobEntity extends Entity {

    /** The EntityAI of this MobEntity. */
    private EntityAI ai;
    // private ItemTable items;

    /**
     * 
     * Constructs a new MobEntity with the specified x, y, width and height. The
     * MobEntity is initially facing south, has a
     * 
     * speed of 2 in both the x and y directions and a red color. Its attack and
     * damage cooldowns are set to 500 milliseconds.
     * 
     * It is given Red, Green and Blue keys and the pickupItems flag is set to true.
     * The hostile flag is also set to true.
     * 
     * @param x      the x position of the MobEntity
     * 
     * @param y      the y position of the MobEntity
     * 
     * @param width  the width of the MobEntity
     * 
     * @param height the height of the MobEntity
     */
    public MobEntity(float x, float y, float width, float height) {
        super(x, y, width, height);
        super.setFacing(Direction.SOUTH);
        super.setDx(2);
        super.setDy(2);
        super.setColor(Renderer.toIntRGB(200, 0, 0));

        super.setAttackCooldownMS(500);
        super.setDamageCooldownMS(500);

        addItems(new RedKey(this), 0);
        addItems(new GreenKey(this), 0);
        addItems(new BlueKey(this), 0);

        add(pickupItems);

        set(pickupItems, true);

        add(hostile);
    }

    /**
     * Causes this MobEntity to take damage from the specified Entity. If the
     * MobEntity's health drops below 0, it is destroyed.
     * If the Entity that caused the damage is a Projectile, the Entity that fired
     * the Projectile is set as the hitBy Entity.
     * If the hitBy Entity has the pickupItems flag set to true, it picks up all the
     * items in this MobEntity's inventory.
     * If the hitBy Entity is the player, there is a 10% chance that the MobEntity
     * will drop a RedKey if it is in the
     * DungeonsMap and has the hostile flag set to true.
     * 
     * @param e     the GameUpdateEvent for this game update
     * @param hitBy the Entity that caused the damage
     */
    @Override
    public void damage(GameUpdateEvent e, Entity hitBy) {
        super.damage(e, hitBy);
        if (getHealth() <= 0) {

            if (hitBy instanceof Projectile)
                hitBy = (Entity) ((Projectile) hitBy).getOrigin();

            // if player hasnt gotten the red key yet
            if (e.getMap().getPlayer().getObtainedKeys()[0] == false) {

                if (getCurMap().equals("DungeonsMap") && is(MobEntity.hostile)) {

                    double chance = Math.random();
                    // System.out.println(chance);

                    // 10 percent chance to get key
                    if (chance < 0.1) {
                        addItems(new RedKey(this), 1);
                    }
                }
            }

            if (hitBy.is(pickupItems)) {
                hitBy.pickupItems(this.getItems());
            }
        }
    }

    /**
     * Calls the reinit method of the superclass and initializes the EntityAI for
     * this MobEntity if it exists.
     */
    @Override
    public void reinit() {
        super.reinit();
        if (ai != null)
            ai.reinit();
    }

    /**
     * Calls the update method of the superclass and updates the EntityAI for this
     * MobEntity if it exists.
     * 
     * @param e the GameUpdateEvent for this game update
     */
    @Override
    public void update(GameUpdateEvent e) throws Exception {
        super.update(e);

        if (ai != null)
            ai.next(e);
    }

    /**
     * If this MobEntity is in the DungeonsMap and has the light flag set to
     * NO_LIGHT, sets the color filter to be
     * darker than the default color filter. Calls the paint method of the
     * superclass.
     * 
     * @param e the PaintEvent to use for painting
     */
    @Override
    public void paint(PaintEvent e) {
        ColorFilter cf = getColorFilter();

        if (getCurMap().equals("DungeonsMap") && getLight() == Light.NO_LIGHT)
            setColorFilter(new ColorFilter(
                    cf.getRed() - 100,
                    cf.getGreen() - 100,
                    cf.getBlue() - 100));

        super.paint(e);
        setColorFilter(cf);
    }

    
    /** 
     * @param e
     */
    @Override
    public void gameClick(GameUpdateEvent e) {
    }

    
    /** 
     * @return EntityAI
     */
    // @Override
    // public void collide(GameUpdateEvent ev, Entity contact) {
    // super.collide(ev, contact);

    // // if(contact instanceof Item){
    // // Item c = (Item)contact;
    // // if(is(pickupItems) && items != null){
    // // getItems().addItem(c);
    // // }
    // // }
    // }

    // ------ getters and setters -------------------------------------------

    /**
     * Returns the EntityAI for this MobEntity.
     * 
     * @return the EntityAI for this MobEntity
     */
    public EntityAI getAI() {
        return this.ai;
    }

    /**
     * Sets the EntityAI for this MobEntity to the specified EntityAI.
     * 
     * @param ai the EntityAI to set for this MobEntity
     */
    public void setAI(EntityAI ai) {
        this.ai = ai;
    }

    // public ItemTable getItems() {
    // return this.items;
    // }

    // public void setItems(ItemTable items) {
    // this.items = items;
    // }

    // ----------- game tag / property keys ------------------------------

    public static String hostile = "hostile";

    // -------------------------------------------------------------------

}
