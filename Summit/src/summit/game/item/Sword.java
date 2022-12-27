package summit.game.item;

import summit.game.entity.Entity;
import summit.gfx.Sprite;

public class Sword extends MeleeWeapon {

    public Sword(Entity owner) {
        super(owner);
        super.setSprite(Sprite.STONE_SWORD);
        super.setBaseDamage(2.5f);
        super.setTextName("sword");
    }

    @Override
    public void use() {
        //do nothing
    }

    @Override
    public Item copy(){
        return new Sword(getOwner());
    }
}
