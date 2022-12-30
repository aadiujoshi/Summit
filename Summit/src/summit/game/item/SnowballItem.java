package summit.game.item;

import summit.game.GameUpdateEvent;
import summit.game.entity.Entity;
import summit.game.entity.projectile.Snowball;
import summit.gfx.Sprite;

public class SnowballItem extends ProjectileWeapon {

    public SnowballItem(Entity owner) {
        super(owner);
        super.setSprite(Sprite.SNOWBALL);
        super.setBaseDamage(0);
        super.setTextName("snowballs");
        super.setProjType(Snowball.class);
    }

    @Override
    public boolean useWeapon(float targetX, float targetY, GameUpdateEvent e){
        boolean b = false;
        if(getOwner().countItems(Sprite.SNOWBALL) > 0){
            getOwner().useItem(Sprite.SNOWBALL);
            b = super.useWeapon(targetX, targetY, e);

            var sLeft = getOwner().getItems().get(Sprite.SNOWBALL);

            if(sLeft.isEmpty())
                getOwner().setEquipped(null);
            else{
                getOwner().setEquipped((WeaponItem)sLeft.peek());
            }
        }

        return b;
    }

    @Override
    public void reinit(){
        super.setProjType(Snowball.class);
    }
    
    @Override
    public void use(){
        super.setUsed(true);
    }

    @Override
    public Item copy(){
        return new SnowballItem(getOwner());
    }
}
