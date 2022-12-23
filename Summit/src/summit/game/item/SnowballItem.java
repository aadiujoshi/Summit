package summit.game.item;

import summit.game.entity.Entity;
import summit.game.entity.mob.MobEntity;
import summit.gfx.OrderPaintEvent;
import summit.gfx.Sprite;

public class SnowballItem extends WeaponItem {

    public SnowballItem(Entity owner) {
        super(owner);
        super.setSprite(Sprite.SNOWBALL);
        super.setTextName("snowballs");
    }

    @Override
    public Item copy(){
        return new SnowballItem(getOwner());
    }
}
