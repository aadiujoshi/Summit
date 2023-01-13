package summit.game.item;

import summit.game.entity.Entity;
import summit.game.entity.mob.Player;
import summit.gfx.Sprite;
import summit.util.Settings;

public class Sword extends MeleeWeapon {

    public Sword(Entity owner) {
        super(owner);
        super.setSprite(Sprite.STONE_SWORD);

        int dif = (int)Settings.getSetting("difficulty");

        if(dif == 1)
            super.setBaseDamage(1.5f);
        if(dif == 2)
            super.setBaseDamage(2.5f);
        if(dif == 3)
            super.setBaseDamage(3.5f);

        if(owner instanceof Player)
            super.setBaseDamage(2.5f);

        super.setAttackRange(1.25f);
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
