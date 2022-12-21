package summit.game.tile;

import summit.gfx.Renderer;
import summit.gfx.Sprite;

public class IceTile extends Tile{
    
    public IceTile(float x, float y) {
        super(x, y);
        super.setBreakable(3);
        super.setSprite(Sprite.ICE_TILE);
        super.setRenderOp(Renderer.NO_OP);
    }
    
}
