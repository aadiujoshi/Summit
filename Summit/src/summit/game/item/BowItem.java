package summit.game.item;

import summit.game.entity.Entity;
import summit.game.entity.mob.MobEntity;
import summit.gfx.Sprite;

public class BowItem extends WeaponItem{
    public BowItem(Entity owner) {
        super(owner);
        super.setSprite(Sprite.BOW);
        super.setBaseDamage(1);
        super.setTextName("bow");
    }

    @Override
    public Item copy(){
        return new BowItem(getOwner());
    }
}
