package summit.game.tile;

import summit.game.GameUpdateEvent;
import summit.gfx.Sprite;

public class GrassTile extends Tile{

    public GrassTile(float x, float y) {
        super(x, y);
        super.setSprite(Sprite.GRASS_TILE);
        
    }

    @Override
    public void gameClick(GameUpdateEvent e) {
        
    }
}
