package summit.game.entity.projectile;

import summit.game.GameUpdateEvent;
import summit.game.animation.GlistenAnimation;
import summit.gfx.Sprite;
import summit.util.GameObject;

public class IceShard extends Projectile{

    public IceShard(GameObject origin, float angle, float damage) {
        super(origin, angle, 20, 0.5f, 0.5f);
        super.setAttackDamage(damage);
        super.setSprite(Sprite.ICE_SHARD);
    }
    
    @Override
    public void update(GameUpdateEvent e) throws Exception{
        super.update(e);
        if(Math.random() > 0.69)
            e.getMap().addAnimation(new GlistenAnimation(getX(), getY(), (int)(Math.random()*5+1), 0x3792CB));
    }
}
