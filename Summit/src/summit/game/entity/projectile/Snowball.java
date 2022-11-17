package summit.game.entity.projectile;

import java.awt.event.MouseEvent;

import summit.game.GameMap;
import summit.game.GameUpdateEvent;
import summit.game.entity.Entity;
import summit.gfx.OrderPaintEvent;
import summit.gfx.PaintEvent;
import summit.gfx.RenderLayers;

public class Snowball extends Projectile {

    public Snowball(float x, float y, float dx, float dy) {
        super(x, y, dx, dy, 0.5f, 0.5f);
    }

    @Override
    public void update(GameUpdateEvent e){
        super.update(e);
        
    }

    @Override
    public void paint(PaintEvent e) {
        
    }

    @Override
    public void gameClick(GameMap map, MouseEvent e) {
        
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

    @Override
    public void setRenderLayer(OrderPaintEvent ope) {
        // TODO Auto-generated method stub
        
    }
}
