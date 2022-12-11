/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.tile;

import summit.game.GameUpdateEvent;
import summit.game.entity.Entity;
import summit.gfx.Light;
import summit.gfx.Sprite;

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
        e.set(Entity.onFire, true);
    }
}
