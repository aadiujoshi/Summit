package summit.game.item;

import summit.game.entity.Entity;
import summit.game.entity.mob.MobEntity;
import summit.gfx.Sprite;

/**
 * 
 * The StickItem class represents a stick item in the game. It is a subclass of
 * the {@link Item} class
 * 
 * and represents an item that can be picked up and used by an {@link Entity}
 * object.
 * 
 * The stick item has a sprite represented by the {@link Sprite} class and a
 * text name "sticks".
 * 
 * Subclasses of this class can override the {@link #use()} method to define
 * specific behavior when the item is used.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R.
 */
public class StickItem extends Item {

    /**
     * 
     * Creates a new instance of the StickItem class with the specified owner.
     * 
     * @param owner The {@link Entity} object that will own this item.
     */
    public StickItem(Entity owner) {
        super(owner);
        super.setSprite(Sprite.STICK_ITEM);
        super.setTextName("sticks");
    }

    /**
     * 
     * Creates a new instance of the StickItem class with the same owner as the
     * current instance.
     * 
     * @return A new instance of the StickItem class with the same owner as the
     *         current instance.
     */
    @Override
    public Item copy() {
        return new StickItem(getOwner());
    }
}
