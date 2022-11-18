package summit.game.tile;

import summit.game.GameClickEvent;
import summit.gfx.Sprite;

public class WoodPlank extends Tile{

    public WoodPlank(float x, float y) {
        super(x, y);
        super.setSprite(Sprite.WOOD_PLANK);
    }

    @Override
    public void gameClick(GameClickEvent e) {
        // TODO Auto-generated method stub
        
    }
    
}
