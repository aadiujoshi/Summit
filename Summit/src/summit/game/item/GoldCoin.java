package summit.game.item;

import summit.game.entity.Entity;
import summit.game.entity.mob.MobEntity;
import summit.gfx.Sprite;

public class GoldCoin extends Item {

    public GoldCoin(Entity owner) {
        super(owner);
        super.setSprite(Sprite.GOLD_COIN);
        super.setTextName("gold");
    }

    @Override
    public Item copy(){
        return new GoldCoin(getOwner());
    }
}
