package summit.game.tile;

import summit.game.GameUpdateEvent;
import summit.gfx.Renderer;
import summit.gfx.Sprite;

public class WoodPlank extends Tile{

    public WoodPlank(float x, float y) {
        super(x, y);
        super.setSprite(Sprite.WOOD_PLANK);
        super.setRenderOp(Renderer.NO_OP);
    }

    @Override
    public void update(GameUpdateEvent e) {
        // TODO Auto-generated method stub
        
    }
}
