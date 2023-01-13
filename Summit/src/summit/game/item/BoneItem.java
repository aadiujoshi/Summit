package summit.game.item;

import summit.game.entity.Entity;
import summit.game.entity.mob.MobEntity;
import summit.gfx.Sprite;

/**
 *
 * 
 * The BoneItem class represents a bone item in the game. It is a subclass of
 * 
 * the {@link Item} class and represents an
 * 
 * item that can be used for various purposes in the game, such as crafting or
 * trading.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R.
 */
public class BoneItem extends Item {

    /**
     *
     * 
     * Creates a new instance of the BoneItem class and sets the owner of the item
     * to the specified entities.
     * 
     * @param owner The entities that owns this item
     */
    public BoneItem(Entity owner) {
        super(owner);
        super.setSprite(Sprite.BONE_ITEM);
        super.setTextName("bones");
    }

    /**
     *
     * 
     * Creates a copy of the current bone item.
     * 
     * @return A copy of the current bone item.
     */
    @Override
    public Item copy() {
        return new BoneItem(getOwner());
    }
}
