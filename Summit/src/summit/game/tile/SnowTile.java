package summit.game.tile;

import summit.game.GameMap;
import summit.game.GameUpdateEvent;
import summit.gfx.ColorFilter;
import summit.gfx.Light;
import summit.gfx.PaintEvent;
import summit.gfx.RenderLayers;
import summit.gfx.Sprite;
import summit.util.GameRegion;
import summit.game.entity.Entity;

public class SnowTile extends Tile{
    
    public SnowTile(float x, float y){
        super(x, y);
        super.setSprite(Sprite.SNOW_TILE);
        super.setBreakable(true);
        Light l = new Light(x, y, 1f, 100, 100, 100);
        l.setRenderLayer(RenderLayers.STRUCTURE_ENTITY_LAYER-1);
    }

    @Override
    public void paint(PaintEvent e){
        super.paint(e);
    }

    @Override
    public void update(GameUpdateEvent e) {
        
    }
}