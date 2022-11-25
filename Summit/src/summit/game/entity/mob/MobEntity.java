package summit.game.entity.mob;

import summit.game.ai.EntityAI;
import summit.game.entity.Entity;
import summit.game.item.itemtable.Inventory;
import summit.util.Direction;

public abstract class MobEntity extends Entity{

    private Direction facing;
    private EntityAI ai;
    
    public MobEntity(float x, float y, float width, float height) {
        super(x, y, width, height);
        facing = Direction.SOUTH;
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
