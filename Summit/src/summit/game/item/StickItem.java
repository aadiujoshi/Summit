package summit.game.item;

import summit.game.entity.Entity;
import summit.game.entity.mob.MobEntity;
import summit.gfx.Sprite;

public class StickItem extends Item {

    public StickItem(Entity owner) {
        super(owner);
        super.setSprite(Sprite.STICK_ITEM);
        super.setTextName("sticks");
    }

    @Override
    public Item copy(){
        return new StickItem(getOwner());
    }
}
