package summit.game.item;

import summit.game.GameUpdateEvent;
import summit.game.entity.Entity;
import summit.gfx.Sprite;

/**
 * 
 * The AppleItem class represents an apple item in the game. It is a subclass of
 * the {@link Item} class and represents an
 * 
 * item that can be used to restore health to the player.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R. S.
 */
public class AppleItem extends Item {

    /**
     * 
     * Creates a new instance of the AppleItem class and sets the owner of the item
     * to the specified Entity.
     * 
     * @param owner The Entity that owns this item
     */
    public AppleItem(Entity owner) {
        super(owner);
        super.setSprite(Sprite.APPLE_ITEM);
        super.setTextName("apples");
    }

    /**
     * 
     * Uses the apple item to restore health to the player. It will only have an
     * effect if the player's health is less than
     * 
     * the maximum health.
     */
    @Override
    public void use() {
        if (getOwner().getHealth() == getOwner().getMaxHealth())
            return;

        getOwner().modHealth(0.5f);
        setUsed(true);
    }

    /**
     * 
     * Creates a copy of the current apple item.
     * 
     * @return A copy of the current apple item.
     */
    @Override
    public Item copy() {
        return new AppleItem(getOwner());
    }
}
