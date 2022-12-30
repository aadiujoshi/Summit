package summit.game.item;

import summit.game.GameUpdateEvent;
import summit.game.entity.Entity;
import summit.game.entity.projectile.Arrow;
import summit.gfx.Sprite;

public class Bow extends ProjectileWeapon{

    public Bow(Entity owner) {
        super(owner);
        super.setSprite(Sprite.BOW);
        super.setBaseDamage(1);
        super.setTextName("bow");
        super.setProjType(Arrow.class);
    }
    
    @Override
    public boolean useWeapon(float targetX, float targetY, GameUpdateEvent e){
        if(getOwner().countItems(Sprite.ARROW_ITEM) > 0)
            return super.useWeapon(targetX, targetY, e);
        return false;
    }

    @Override
    public void reinit(){
        super.setProjType(Arrow.class);
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
