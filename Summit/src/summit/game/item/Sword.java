package summit.game.item;

import summit.game.entity.mob.MobEntity;
import summit.gfx.Sprite;

public class Sword extends WeaponItem {

    public Sword(MobEntity owner) {
        super(owner);
        super.setSprite(Sprite.STONE_SWORD);
        super.setBaseDamage(2.5f);
    }

    @Override
    public Item copy(){
        return new Sword(getOwner());
    }
}
