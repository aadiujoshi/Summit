package summit.game.entity;

import summit.util.Direction;

public abstract class MobEntity extends Entity{

    private Direction facing;

    public MobEntity(float x, float y, float width, float height) {
        super(x, y, width, height);
        facing = Direction.NORTH;
    }

    //------  getters and setters -------------------------------------------
    
    public Direction getFacing() {
        return this.facing;
    }

    public void setFacing(Direction facing) {
        this.facing = facing;
    }

}
