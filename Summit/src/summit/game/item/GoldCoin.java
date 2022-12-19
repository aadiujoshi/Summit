package summit.game.item;

import summit.game.entity.mob.MobEntity;
import summit.gfx.Sprite;

public class GoldCoin extends Item {

    public GoldCoin(MobEntity owner) {
        super(owner);
        super.setSprite(Sprite.GOLD_COIN);
    }

    @Override
    public Item copy(){
        return new GoldCoin(getOwner());
    }
}
