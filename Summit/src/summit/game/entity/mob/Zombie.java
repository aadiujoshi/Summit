package summit.game.entity.mob;

import summit.game.GameUpdateEvent;
import summit.game.ai.HostileMobAI;
import summit.game.entity.Entity;
import summit.gfx.ColorFilter;
import summit.gfx.PaintEvent;
import summit.gfx.Sprite;

public class Zombie extends HumanoidEntity{

    public Zombie(float x, float y) {
        super(x, y, 1, 2);
        super.setAI(new HostileMobAI(this));
        super.setHealth(5);
        super.setSprite(Sprite.PLAYER_FACE_BACK_1);
        super.setMaxHealth(5);
    }

    @Override
    public void paint(PaintEvent e){
        setColorFilter(new ColorFilter(0, 100, 0));
        super.paint(e);
    }
    
    @Override
    public void collide(Entity g) {
        // TODO Auto-generated method stub
        
    }
    
}
