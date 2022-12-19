package summit.game.item;

import summit.game.entity.mob.MobEntity;
import summit.gfx.Sprite;

public class StickItem extends Item {

    public StickItem(MobEntity owner) {
        super(owner);
        super.setSprite(Sprite.STICK_ITEM);
    }

    @Override
    public Item copy(){
        return new StickItem(getOwner());
    }
}
