package summit.game.item;

import summit.game.entity.Entity;
import summit.gfx.Sprite;

/**
 *
 * 
 * The GreenKey class represents a green key item in the game. It is a subclass
 * of
 * 
 * the {@link Item} class and represents a key
 * 
 * that can be used to unlock green colored doors or other objects in the game.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R. S.
 */
public class GreenKey extends Item {

    /**
     *
     * 
     * Creates a new instance of the GreenKey class and sets the owner of the item
     * to the specified Entity.
     * 
     * @param owner The Entities that owns this item
     */
    public GreenKey(Entity owner) {
        super(owner);
        super.setSprite(Sprite.GREEN_KEY);
        super.setTextName("green key");
    }

    /**
     *
     * 
     * Creates a copy of the current green key item.
     * 
     * @return A copy of the current green key item.
     */
    @Override
    public Item copy() {
        return new GreenKey(getOwner());
    }
}
