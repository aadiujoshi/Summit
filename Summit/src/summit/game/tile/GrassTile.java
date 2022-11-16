package summit.game.tile;

import java.awt.event.MouseEvent;

import summit.game.GameMap;
import summit.game.GameUpdateEvent;
import summit.gfx.PaintEvent;
import summit.gfx.Sprite;

public class GrassTile extends Tile{

    public GrassTile(float x, float y) {
        super(x, y);
        super.setSprite(Sprite.GRASS_TILE);
        
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
