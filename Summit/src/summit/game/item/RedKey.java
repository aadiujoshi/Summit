package summit.game.item;

import summit.game.entity.Entity;
import summit.gfx.Sprite;

/**
 * 
 * The RedKey class represents a red key in the game. It extends the Item class
 * and provides a basic implementation for properties
 * 
 * and methods specific to a red key, such as the sprite and text name of the
 * key. It also overrides the copy() method to return a
 * 
 * copy of the key.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R.
 */
public class RedKey extends Item {

    /**
     * 
     * Creates a new instance of the RedKey class and sets the owner of the key to
     * the specified entity.
     * Also sets the sprite and text name of the key.
     * 
     * @param owner The entity that owns the key
     */
    public RedKey(Entity owner) {
        super(owner);
        super.setSprite(Sprite.RED_KEY);
        super.setTextName("red key");
    }

    /**
     * 
     * Creates a copy of the current red key.
     * 
     * @return A copy of the current red key
     */
    @Override
    public Item copy() {
        return new RedKey(getOwner());
    }
}
