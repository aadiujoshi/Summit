package summit.game.ai;

import summit.game.GameUpdateEvent;
import summit.game.entity.Entity;

public interface EntityAI {
    public void next(GameUpdateEvent e, Entity ent);
}
