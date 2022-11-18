package summit.game.tile;

import summit.game.GameClickEvent;
import summit.gfx.Light;
import summit.gfx.PaintEvent;
import summit.gfx.RenderLayers;
import summit.gfx.Sprite;

public class SnowTile extends Tile{
    
    public SnowTile(float x, float y){
        super(x, y);
        super.setSprite(Sprite.SNOW_TILE);
        Light l = new Light(x, y, 1f, 100, 100, 100);
        l.setRenderLayer(RenderLayers.STRUCTURE_ENTITY_LAYER-1);
        super.setLight(l);
    }

    @Override
    public void gameClick(GameClickEvent e){
        
        System.out.println(getX() + "  " + getY());

        this.setDestroy(true);
    }
}