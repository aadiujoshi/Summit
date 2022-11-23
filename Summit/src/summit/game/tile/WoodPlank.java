package summit.game.tile;

import summit.game.GameClickEvent;
import summit.gfx.Renderer;
import summit.gfx.Sprite;

public class WoodPlank extends Tile{

    public WoodPlank(float x, float y) {
        super(x, y);
        super.setSprite(Sprite.WOOD_PLANK);
        super.setRenderOp(Renderer.NO_OP);
    }

    @Override
    public void gameClick(GameClickEvent e) {
        // TODO Auto-generated method stub
        
    }
    
}
