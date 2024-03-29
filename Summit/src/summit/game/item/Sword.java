package summit.game.item;

import summit.game.entity.Entity;
import summit.game.entity.mob.Player;
import summit.gfx.Sprite;
import summit.util.Settings;

/**
 * 
 * The Sword class represents a sword item in the game. It is a subclass of the
 * {@link MeleeWeapon} class
 * 
 * and represents an item that can be picked up and used by an {@link Entity}
 * object to attack other entities.
 * 
 * The sword item has a sprite represented by the {@link sprite} class, a base
 * damage of 2.5, attack range of 1.25 and
 * 
 * a text name "sword".
 * 
 * Subclasses of this class can override the {@link #use()} method to define
 * specific behavior when the item is used.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R. S.
 */
public class Sword extends MeleeWeapon {

    /**
     * 
     * Creates a new instance of the Sword class with the specified owner.
     * 
     * @param owner The {@link Entity} object that will own this item.
     */
    public Sword(Entity owner) {
        super(owner);
        super.setSprite(Sprite.STONE_SWORD);

        int dif = (int)Settings.getSetting("difficulty");

        if(dif == 1)
            super.setBaseDamage(1.5f);
        if(dif == 2)
            super.setBaseDamage(2.5f);
        if(dif == 3)
            super.setBaseDamage(3.5f);

        if(owner instanceof Player)
            super.setBaseDamage(2.5f);

        super.setAttackRange(1.25f);
        super.setTextName("sword");
    }

    /**
     * 
     * This method is empty, This method is called every time the sword is used.
     * It can be overridden by subclasses to define specific behavior when the sword
     * is used.
     */
    @Override
    public void use() {
        // do nothing
    }

    /**
     * 
     * Returns a copy of the current Sword object.
     * 
     * @return A copy of the current Sword object.
     */
    @Override
    public Item copy() {
        return new Sword(getOwner());
    }
}
