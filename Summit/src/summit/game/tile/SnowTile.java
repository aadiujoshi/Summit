package summit.game.tile;

import java.awt.event.MouseEvent;

import summit.game.GameMap;
import summit.game.GameUpdateReciever;
import summit.game.GameUpdateEvent;
import summit.gfx.PaintEvent;
import summit.gfx.Renderer;
import summit.gfx.Sprite;

public class SnowTile extends Tile{
    
    public SnowTile(float x, float y){
        super(x, y);
        super.setSprite(Sprite.SNOW_TILE);
    }
    
    @Override
    public void gameClick(GameMap map, MouseEvent e){
        
    }

    @Override
    public void update(GameUpdateEvent e) {
        
    }
}