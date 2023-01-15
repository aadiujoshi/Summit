package summit.game.item;

import summit.game.entity.Entity;
import summit.gfx.Sprite;

/**
 *
 * 
 * The BlueKey class represents a blue key item in the game. It is a subclass of
 * the
 * 
 * {@link Item} class and represents a key that can be used to open blue locked
 * doors.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R. S.
 */
public class BlueKey extends Item {

    /**
     *
     * 
     * Creates a new instance of the BlueKey class and sets the owner of the item
     * to the specified {@link Entity}.
     * 
     * @param owner The {@link Entity} that owns this item
     */
    public BlueKey(Entity owner) {
        super(owner);
        super.setSprite(Sprite.BLUE_KEY);
        super.setTextName("blue key");
    }

    /**
     *
     * 
     * Creates a copy of the current BlueKey item.
     * 
     * @return A copy of the current BlueKey item.
     */
    @Override
    public Item copy() {
        return new BlueKey(getOwner());
    }
}
