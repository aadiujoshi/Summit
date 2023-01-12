package summit.game.item;

import summit.game.entity.Entity;
import summit.game.entity.mob.MobEntity;
import summit.gfx.Sprite;

/**
 * The ArrowItem class represents an arrow item in the game. It is a subclass of
 * 
 * the {@link Item} class and represents an
 * 
 * item that can be used as a projectile for ranged attacks.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R.
 */
public class ArrowItem extends Item {

    /**
     *
     * 
     * Creates a new instance of the ArrowItem class and sets the owner of the item
     * to the specified entities.
     * 
     * @param owner The entities that owns this item
     */
    public ArrowItem(Entity owner) {
        super(owner);
        super.setSprite(Sprite.ARROW_ITEM);
        super.setTextName("arrows");
    }

    /**
     *
     * 
     * Creates a copy of the current arrow item.
     * 
     * @return A copy of the current arrow item.
     */
    @Override
    public Item copy() {
        return new ArrowItem(getOwner());
    }
}
