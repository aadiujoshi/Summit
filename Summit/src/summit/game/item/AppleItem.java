package summit.game.item;

import summit.game.entity.mob.MobEntity;
import summit.gfx.Sprite;

public class AppleItem extends Item {

    public AppleItem(MobEntity owner) {
        super(owner);
        super.setSprite(Sprite.APPLE_ITEM);
    }

    @Override
    public void use() {
        getOwner().modHealth(0.5f);

        setUsed(true);
    }
    
    @Override
    public Item copy(){
        return new AppleItem(getOwner());
    }
}
