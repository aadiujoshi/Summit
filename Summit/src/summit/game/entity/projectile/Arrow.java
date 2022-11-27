package summit.game.entity.projectile;

import java.awt.event.MouseEvent;

import summit.game.GameUpdateEvent;
import summit.game.GameMap;
import summit.game.GameUpdateEvent;
import summit.game.entity.Entity;
import summit.gfx.OrderPaintEvent;
import summit.gfx.PaintEvent;
import summit.gfx.RenderLayers;

public class Arrow extends Projectile {

    public Arrow(float x, float y, float dx, float dy) {
        super(x, y, dx, dy, 0.5f, 0.25f);
    }

    @Override
    public void paint(PaintEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void gameClick(GameUpdateEvent e) {
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
