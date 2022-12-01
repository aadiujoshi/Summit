/*
* BPA project by Aadi Joshi, Aditya Bhattaharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.tile;

import summit.game.GameUpdateEvent;
import summit.gfx.Light;
import summit.gfx.PaintEvent;
import summit.gfx.RenderLayers;
import summit.gfx.Sprite;
import summit.util.GameRegion;
import summit.game.entity.Entity;

public class LavaTile extends Tile{

    public LavaTile(float x, float y) {
        super(x, y);
        super.setSprite(Sprite.LAVA_TILE);
        Light glow = new Light(x, y, 1f, 255, 163, 0);
        super.setLight(glow); Math.random();

    }
    
    @Override
    public void update(GameUpdateEvent e) {

    }

    @Override
    public void collide(Entity e) {
        super.collide(e);
        e.setOnFire(true);
        
    }
}
