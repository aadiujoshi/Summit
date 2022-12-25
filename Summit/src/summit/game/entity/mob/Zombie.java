/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.entity.mob;

import summit.game.GameUpdateEvent;
import summit.game.ai.HostileMobAI;
import summit.game.entity.Entity;
import summit.game.item.GoldCoin;
import summit.game.item.Sword;
import summit.gfx.ColorFilter;
import summit.gfx.PaintEvent;
import summit.gfx.Renderer;
import summit.gfx.Sprite;

public class Zombie extends HumanoidEntity{

    public Zombie(float x, float y) {
        super(x, y);
        super.setAI(new HostileMobAI(this));
        super.setAttackRange(0.5f);
        super.setHealth(5);
        super.setEquipped(new Sword(this));
        super.setSprite(Sprite.PLAYER_FACE_BACK_1);
        super.setMaxHealth(5);
        super.setAttackDamage(1);

        super.addItems(new GoldCoin(this), (int)(Math.random()*10));

        super.set(hostile, true);
    }

    @Override
    public void paint(PaintEvent e){
        if(!is(damageCooldown)){
            setColorFilter(new ColorFilter(0, 100, 0));
            
            // if(getCurMap().equals("DungeonsMap")){
            //     setColorFilter(new ColorFilter(0, 60, 0));
            // }
        }
        
        super.paint0(e);

        // e.getRenderer().renderGame(Sprite.STONE_SWORD, getX()+0.25f, getY()-0.25f, Renderer.NO_OP, ColorFilter.NOFILTER, e.getCamera());
    }
}
