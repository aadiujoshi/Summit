package summit.game.entity.projectile;

import summit.game.entity.Entity;
import summit.gfx.Light;

public abstract class Projectile extends Entity {

    public Projectile(float x, float y, float dx, float dy, float width, float height) {
        super(x, y, width, height);
        super.setDx(dx);
        super.setDy(dy);
        super.setLight(new Light(x, y, 0.25f, -50, -50, -50));
        super.setHealth(1);
    }

    @Override
    public void collide(Entity e) {
        e.damage(getHitDamage(), e);
        
    }
}
