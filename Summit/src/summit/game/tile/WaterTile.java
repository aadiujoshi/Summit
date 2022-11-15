package summit.game.tile;

import java.awt.event.MouseEvent;

import summit.game.GameMap;
import summit.game.GameUpdateEvent;
import summit.gfx.Light;
import summit.gfx.PaintEvent;
import summit.gfx.Sprite;
import summit.util.Time;

public class WaterTile extends Tile{

    private long lastAnimationChange = Time.timeMs();

    public WaterTile(float x, float y) {
        super(x, y);
        super.setSprite(Sprite.WATER_TILE);
        super.setBoundary(false);
        super.setLight(new Light(x, y, 2, 0, 0, 100));
    }

    @Override
    public void paint(PaintEvent e){
        e.renderLater(getLight());
        if(Time.timeMs() - lastAnimationChange > 300){
            setRotation((int)(Math.random()*5));
            lastAnimationChange = Time.timeMs();
        }
        super.paint(e);
    }

    @Override
    public void gameClick(GameMap map, MouseEvent e) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void update(GameUpdateEvent e) {
        // TODO Auto-generated method stub
        
    }
    
}
