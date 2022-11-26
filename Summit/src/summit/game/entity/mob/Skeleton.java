package summit.game.entity.mob;

import summit.game.GameUpdateEvent;
import summit.game.ai.HostileMobAI;
import summit.game.entity.Entity;
import summit.gfx.PaintEvent;
import summit.gfx.Renderer;
import summit.gfx.Sprite;

public class Skeleton extends HumanoidEntity{

    public Skeleton(float x, float y) {
        super(x, y, 1, 2);
        super.setSprite(Sprite.SKELETON_FACE_BACK);
        super.setAI(new HostileMobAI());
        super.setMaxHealth(10);
        super.setHealth(getMaxHealth());
    }

    @Override
    public void paint(PaintEvent e){
        super.paint(e);

        // e.getRenderer().renderGame(Sprite.BOW, getX()+0.35f, getY()-0.2f, 
        //                             getRenderOp() | Renderer.ROTATE_90 & ~Renderer.FLIP_X, 
        //                             getColorFilter(), 
        //                             e.getCamera());
    }

    @Override
    public void gameClick(GameUpdateEvent e) {
        
    }

    @Override
    public void damage(float damage, Entity e) {
        
    }

    @Override
    public void destroy(GameUpdateEvent ge) {
        
    }

    @Override
    public void collide(Entity g) {
        
    }
}
