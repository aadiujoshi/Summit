package summit.game.item;

import summit.game.entity.Entity;
import summit.game.entity.mob.MobEntity;
import summit.gfx.Sprite;

public class ArrowItem extends Item {

    public ArrowItem(Entity owner) {
        super(owner);
        super.setSprite(Sprite.ARROW_ITEM);
        super.setTextName("arrows");
    }

    @Override
    public Item copy(){
        return new ArrowItem(getOwner());
    }
}
