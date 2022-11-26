package summit.game.entity.mob;

import summit.game.GameUpdateEvent;
import summit.game.ai.EntityAI;
import summit.game.animation.ParticleAnimation;
import summit.game.entity.Entity;
import summit.gfx.Renderer;
import summit.util.Direction;

public abstract class MobEntity extends Entity{

    private Direction facing;
    private EntityAI ai;
    
    public MobEntity(float x, float y, float width, float height) {
        super(x, y, width, height);
        facing = Direction.SOUTH;
        super.setDx(3);
        super.setDy(3);
        super.setColor(Renderer.toIntRGB(200, 0, 0));
    }

    @Override
    public void update(GameUpdateEvent e){
        super.update(e);
        
        if(ai != null)
            ai.next(e);
    }

    @Override
    public void destroy(GameUpdateEvent e){
        super.destroy(e);

        e.getMap().addParticleAnimation(new ParticleAnimation(getX(), getY(), 
                                        1000, 60, getColor()));
    }

    //------  getters and setters -------------------------------------------
    
    public Direction getFacing() {
        return this.facing;
    }

    public void setFacing(Direction facing) {
        this.facing = facing;
    }

    public EntityAI getAI() {
        return this.ai;
    }

    public void setAI(EntityAI ai) {
        this.ai = ai;
    }
}
