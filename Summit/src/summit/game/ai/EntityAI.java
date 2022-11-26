package summit.game.ai;

import summit.game.GameUpdateEvent;
import summit.game.entity.Entity;

public abstract class EntityAI {

    protected Entity entity;

    public EntityAI(Entity e){
        this.entity = e;
    }

    public abstract void next(GameUpdateEvent e);
}
