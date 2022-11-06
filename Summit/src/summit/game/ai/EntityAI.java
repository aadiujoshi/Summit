package summit.game.ai;

import summit.game.GameMap;

public interface EntityAI {
    public void next(GameMap map, float x, float y);
}
