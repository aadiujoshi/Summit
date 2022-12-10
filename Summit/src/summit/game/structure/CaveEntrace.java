package summit.game.structure;

import summit.game.gamemap.GameMap;
import summit.gfx.Sprite;

public class CaveEntrace extends MapEntrance{
    public CaveEntrace(float x, float y, GameMap exMap, GameMap parentMap) {
        super(x, y, 1, 1, exMap, parentMap);
        super.setSprite(Sprite.CAVE_ENTRACE);
    }
}
