package summit.game.item;

import summit.game.entity.mob.MobEntity;

public abstract class WeaponItem extends Item{

    private float mult;
    private float baseDamage;

    public WeaponItem(MobEntity owner) {
        super(owner);

        baseDamage = 1;
        mult = 1;
    }

    @Override
    public void use() {
        getOwner().setAttackDamage(baseDamage * mult);
    }
    
    public void setDamageMult(float mult) {
        this.mult = mult;
    }
    
    public void setBaseDamage(float baseDamage) {
        this.baseDamage = baseDamage;
    }
}
