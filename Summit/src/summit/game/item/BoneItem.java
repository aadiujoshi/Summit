package summit.game.item;

import summit.game.entity.mob.MobEntity;
import summit.gfx.Sprite;

public class BoneItem extends Item{

    public BoneItem(MobEntity owner) {
        super(owner);
        super.setSprite(Sprite.BONE_ITEM);
    }
    
    @Override
    public Item copy(){
        return new BoneItem(getOwner());
    }
}
