package summit.game.structure;

import summit.game.GameUpdateEvent;
import summit.game.gamemap.GameMap;
import summit.gfx.Sprite;

public class BossRoomExit extends MapEntrance{

    public BossRoomExit(float x, float y, GameMap exMap, GameMap parentMap) {
        super(x, y, 2, 2, exMap, parentMap);
        super.setSprite(Sprite.DOOR);
    }
}
