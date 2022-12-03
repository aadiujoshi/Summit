/*
* BPA project by Aadi Joshi, Aditya Bhattaharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.entity.mob;

import summit.game.GameUpdateEvent;
import summit.game.ai.HostileMobAI;
import summit.game.entity.Entity;
import summit.gfx.ColorFilter;
import summit.gfx.PaintEvent;
import summit.gfx.Renderer;
import summit.gfx.Sprite;

public class Zombie extends HumanoidEntity{

    public Zombie(float x, float y) {
        super(x, y, 1, 2);
        super.setAI(new HostileMobAI(this));
        super.setHealth(5);
        super.setSprite(Sprite.PLAYER_FACE_BACK_1);
        super.setMaxHealth(5);
        super.setHitDamage(0);
    }

    @Override
    public void paint(PaintEvent e){
        if(!damageCooldown()){
            setColorFilter(new ColorFilter(0, 100, 0));
        }
        super.paint(e);
        e.getRenderer().renderGame(Sprite.WOOD_SWORD, getX()+0.25f, getY()-0.25f, Renderer.NO_OP, ColorFilter.NOFILTER, e.getCamera());
    }
    
}
