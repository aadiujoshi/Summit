package summit.game.tile;

import java.awt.event.MouseEvent;

import summit.game.GameMap;
import summit.game.GameUpdateEvent;
import summit.gfx.PaintEvent;
import summit.gfx.Sprite;

public class StoneTile extends Tile{

    public StoneTile(float x, float y) {
        super(x, y);
        super.setSprite(Sprite.STONE_TILE);
        
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
