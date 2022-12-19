package summit.game.item;

import summit.game.entity.mob.MobEntity;
import summit.gfx.Sprite;

public class BowItem extends WeaponItem{
    public BowItem(MobEntity owner) {
        super(owner);
        super.setSprite(Sprite.BOW);
        super.setBaseDamage(1);
    }

    @Override
    public Item copy(){
        return new BowItem(getOwner());
    }
}
