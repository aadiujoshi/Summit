package summit.game.entity.mob;

import summit.game.ai.HostileMobAI;

public abstract class HostileMobEntity extends MobEntity{

    public HostileMobEntity(float x, float y, float width, float height) {
        super(x, y, width, height);
        super.setAI(new HostileMobAI(this));
        super.set(MobEntity.hostile, true);
    }
    
}
