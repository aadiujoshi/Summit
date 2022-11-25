package summit.game.tile;

import summit.game.GameUpdateEvent;
import summit.game.entity.Entity;
import summit.gfx.Sprite;

public class GrassTile extends Tile{

    public GrassTile(float x, float y) {
        super(x, y);
        super.setSprite(Sprite.GRASS_TILE);
        
    }

    @Override
    public void update(GameUpdateEvent e) {
        
    }
}
