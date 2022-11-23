package summit.game.tile;

import java.awt.event.MouseEvent;

import summit.game.GameUpdateEvent;
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
        // super.setLight(new Light(x, y, 1f, 0, 0, 100));
    }

    @Override
    public void paint(PaintEvent e){
        if(Time.timeMs() - lastAnimationChange > 300){
            setRenderOp((int)(Math.random()*5));
            lastAnimationChange = Time.timeMs();
        }
        super.paint(e);
    }

    @Override
    public void gameClick(GameUpdateEvent e) {
        // TODO Auto-generated method stub
        
    }
    
}
