package summit.game.item;

import summit.game.entity.mob.MobEntity;
import summit.gfx.Sprite;

public class SnowballItem extends Item {

    public SnowballItem(MobEntity owner) {
        super(owner);
        super.setSprite(Sprite.SNOWBALL);
    }

    @Override
    public Item copy(){
        return new SnowballItem(getOwner());
    }
}
