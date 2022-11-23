package summit.game.entity.projectile;

import summit.game.GameUpdateEvent;
import summit.game.entity.Entity;
import summit.gfx.Light;
import summit.gfx.PaintEvent;

public abstract class Projectile extends Entity {

    private String sprite;

    public Projectile(float x, float y, float dx, float dy, float width, float height) {
        super(x, y, width, height);
        super.setDx(dx);
        super.setDy(dy);
        super.setLight(new Light(x, y, 0.25f, -50, -50, -50));
        super.setHealth(1);
    }

    @Override
    public void paint(PaintEvent e){
        super.paint(e);
    }

    @Override
    public void update(GameUpdateEvent e){
        super.update(e);
    }
}
