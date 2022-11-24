package summit.game.tile;

import summit.game.GameUpdateEvent;
import summit.game.animation.ScheduledEvent;
import summit.game.animation.Scheduler;
import summit.game.entity.Entity;
import summit.gfx.Sprite;

public class WaterTile extends Tile{

    //refactor to use scheduler
    private ScheduledEvent waterAnimation;

    public WaterTile(float x, float y) {
        super(x, y);
        super.setSprite(Sprite.WATER_TILE);
        super.setBoundary(false);
        // super.setLight(new Light(x, y, 1f, 0, 0, 100));

        this.waterAnimation = new ScheduledEvent(300, ScheduledEvent.FOREVER) {
            @Override
            public void run() {
                setRenderOp((int)(Math.random()*5));
            }
        };

        Scheduler.registerEvent(waterAnimation);
    }

    @Override
    public void update(GameUpdateEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void collide(Entity e) {
        super.collide(e);
        e.setInWater(true);
        e.setOnFire(false);
    }
}
