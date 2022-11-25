package summit.game.tile;

import summit.game.GameUpdateEvent;
import summit.gfx.Renderer;
import summit.gfx.Sprite;

public class StoneTile extends Tile{

    public StoneTile(float x, float y) {
        super(x, y);
        super.setSprite(Sprite.STONE_TILE);
        super.particleAnimation(true);
        super.setParticleColor(Renderer.toIntRGB(100, 100, 100));
    }

    @Override
    public void update(GameUpdateEvent e) {
        // TODO Auto-generated method stub
        
    }

    
}
