package summit.game.item;

import summit.game.entity.mob.MobEntity;
import summit.gfx.Sprite;

public class ArrowItem extends Item {

    public ArrowItem(MobEntity owner) {
        super(owner);
        super.setSprite(Sprite.ARROW_ITEM);
    }

    @Override
    public Item copy(){
        return new ArrowItem(getOwner());
    }
}
