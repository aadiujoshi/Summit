/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.tile;

import summit.game.GameUpdateEvent;
import summit.game.entity.Entity;
import summit.gfx.Sprite;

public class WaterTile extends Tile{
    
    public WaterTile(float x, float y) {
        super(x, y);
        super.setSprite(Sprite.WATER_TILE);
        super.setBoundary(false);
        super.rotateAnimation(true);
        // super.setLight(new Light(x, y, 1f, 0, 0, 100));
    }

    @Override
    public void update(GameUpdateEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void collide(Entity e) {
        super.collide(e);
        e.set(Entity.inWater, true);
        e.set(Entity.onFire, false);
    }
}
