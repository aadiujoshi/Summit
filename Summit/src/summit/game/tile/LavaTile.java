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

    //used for damaging entities
    private Entity dummy;

    public LavaTile(float x, float y) {
        super(x, y);
        super.setSprite(Sprite.LAVA_TILE);
        super.rotateAnimation(true);
        Light glow = new Light(x, y, 1f, 255, 163, 0);
        super.setLight(glow); Math.random();

        dummy = new Entity(getX(), getY(), 1, 1) {
            @Override
            public void gameClick(GameUpdateEvent e) {
            }
        };
        dummy.setAttackDamage(1);
        dummy.setHealth(Integer.MAX_VALUE);
    }

    @Override
    public void collide(Entity e) {
        super.collide(e);
        e.damage(dummy);
        
        e.set(Entity.onFire, true);
    }
}
