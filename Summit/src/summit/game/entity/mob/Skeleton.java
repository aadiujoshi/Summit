/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.entity.mob;

import summit.game.GameUpdateEvent;
import summit.game.ai.HostileMobAI;
import summit.game.entity.Entity;
import summit.game.entity.projectile.Arrow;
import summit.gfx.PaintEvent;
import summit.gfx.Renderer;
import summit.gfx.Sprite;

public class Skeleton extends HumanoidEntity{

    public Skeleton(float x, float y) {
        super(x, y, 1, 2);
        super.setSprite(Sprite.SKELETON_FACE_BACK);
        super.setAI(new HostileMobAI(this));
        super.setAttackRange(4f);
        super.setAttackCooldownMS(1000);
        super.setMaxHealth(7);
        super.setHealth(getMaxHealth());
        super.setAttackDamage(1);

        super.set(hostile, true);
    }

    @Override
    public void paint(PaintEvent e){
        super.paint(e);

        e.getRenderer().renderGame(Sprite.BOW, getX()+0.35f, getY()-0.2f, 
                                    getRenderOp() & ~Renderer.FLIP_X, 
                                    getColorFilter(), 
                                    e.getCamera());
    }

    @Override
    public void attack(Entity e, GameUpdateEvent ev) {
        if(!is(attackCooldown)){
            //inaccuracy of 30 degrees
            ev.getMap().spawn(new Arrow(this, 
                                theta(e.getX(), getX(), 
                                            e.getY(), getY()),// + (float)(Math.random()*(Math.PI/6)-(Math.PI/6)),
                                            1));
            set(attackCooldown, true);
        }
    }
}
