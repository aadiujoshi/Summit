package summit.game.item;

import summit.game.GameUpdateEvent;
import summit.game.entity.Entity;
import summit.game.entity.projectile.Arrow;
import summit.game.entity.projectile.Projectile;
import summit.gfx.Sprite;
import summit.util.Region;

public class Bow extends ProjectileWeapon{

    public Bow(Entity owner) {
        super(owner);
        super.setSprite(Sprite.BOW);
        super.setBaseDamage(1);
        super.setTextName("bow");
    }
    
    @Override
    public boolean useWeapon(float targetX, float targetY, GameUpdateEvent e){
        if(getOwner().countItems(Sprite.ARROW_ITEM) > 0)
            return super.useWeapon(targetX, targetY, e);
        return false;
    }

    @Override
    public Projectile getProjInstance(float tx, float ty){
        return new Arrow(getOwner(),
                            Region.theta(tx, getOwner().getX(), 
                                        ty, getOwner().getY()), 
                            getNetDamage());
    }

    @Override
    public void use() {
        getOwner().useItem(Sprite.ARROW_ITEM);
    }
    
    @Override
    public Item copy(){
        return new Bow(getOwner());
    }
}
