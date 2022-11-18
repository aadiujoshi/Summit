package summit.game.tile;

import summit.game.GameClickEvent;
import summit.gfx.Sprite;

public class SnowTile extends Tile{
    
    public SnowTile(float x, float y){
        super(x, y);
        super.setSprite(Sprite.SNOW_TILE);
        // super.setLight(new Light(x, y, 1f, 100, 100, 100));
    }
    
    @Override
    public void gameClick(GameClickEvent e){
        
        System.out.println(getX() + "  " + getY());

        this.setDestroy(true);
    }
}