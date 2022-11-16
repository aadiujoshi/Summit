package summit.game.entity.projectile;

import java.awt.event.MouseEvent;

import summit.game.GameMap;
import summit.game.GameUpdateEvent;
import summit.game.entity.Entity;
import summit.gfx.PaintEvent;

public class SnowballProjectile extends Projectile {

    public SnowballProjectile(float x, float y, float width, float height) {
        super(x, y, 0.5f, 0.5f);
    }

    @Override
    public void paint(PaintEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void gameClick(GameMap map, MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void damage(GameUpdateEvent ge, Entity e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void destroy(GameUpdateEvent ge) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void collide(Entity e) {
        // TODO Auto-generated method stub
        
    }
    

}
