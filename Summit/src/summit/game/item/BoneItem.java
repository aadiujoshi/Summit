package summit.game.item;

import summit.game.entity.Entity;
import summit.game.entity.mob.MobEntity;
import summit.gfx.Sprite;

public class BoneItem extends Item{

    public BoneItem(Entity owner) {
        super(owner);
        super.setSprite(Sprite.BONE_ITEM);
        super.setTextName("bones");
    }
    
    @Override
    public Item copy(){
        return new BoneItem(getOwner());
    }
}
