package summit.game.tile;

import java.awt.event.MouseEvent;

import summit.game.GameUpdateEvent;
import summit.game.GameMap;
import summit.game.GameUpdateEvent;
import summit.gfx.PaintEvent;
import summit.game.entity.Entity;
import summit.gfx.Sprite;
import summit.util.GameRegion;

public class StoneTile extends Tile{

    public StoneTile(float x, float y) {
        super(x, y);
        super.setSprite(Sprite.STONE_TILE);
        
    }

    @Override
    public void update(GameUpdateEvent e) {
        // TODO Auto-generated method stub
        
    }

    
}
