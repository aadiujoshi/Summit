package summit.game.tile;

import java.awt.event.MouseEvent;

import summit.game.GameMap;
import summit.game.GameUpdateReciever;
import summit.game.GameUpdateEvent;
import summit.gfx.Light;
import summit.gfx.PaintEvent;
import summit.gfx.Renderer;
import summit.gfx.Sprite;

public class SnowTile extends Tile{
    
    public SnowTile(float x, float y){
        super(x, y);
        super.setSprite(Sprite.SNOW_TILE);
        super.setLight(new Light(x, y, 1f, 100, 100, 100));
    }
    
    @Override
    public void gameClick(GameMap map, MouseEvent e){
        
    }

    @Override
    public void update(GameUpdateEvent e) {
        
    }
}