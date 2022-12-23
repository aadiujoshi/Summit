package summit.game.item;

import summit.game.entity.Entity;
import summit.game.entity.mob.MobEntity;
import summit.gfx.Sprite;

public class Sword extends WeaponItem {

    public Sword(Entity owner) {
        super(owner);
        super.setSprite(Sprite.STONE_SWORD);
        super.setBaseDamage(2.5f);
        super.setTextName("sword");
    }

    @Override
    public Item copy(){
        return new Sword(getOwner());
    }
}
