package summit.game.entity.projectile;

import summit.game.GameUpdateEvent;
import summit.game.entity.Entity;
import summit.gfx.Light;

public abstract class Projectile extends Entity {

    public Projectile(float x, float y, float dx, float dy, float width, float height) {
        super(x, y, width, height);
        super.setDx(dx);
        super.setDy(dy);
        super.setLight(new Light(x, y, 0.25f, -50, -50, -50));
    }

    @Override
    public void update(GameUpdateEvent e){
        super.update(e);

        
    }
}
