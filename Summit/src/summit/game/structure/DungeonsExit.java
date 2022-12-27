package summit.game.structure;

import summit.game.gamemap.GameMap;
import summit.gfx.ColorFilter;
import summit.gfx.Sprite;

public class DungeonsExit extends MapEntrance {

    /**
     * exmap is the mainmap and parentmap is the dungeonmap
     */
    public DungeonsExit(float x, float y, GameMap exMap, GameMap parentMap) {
        super(x, y, 1, 1, exMap, parentMap);
        super.setSprite(Sprite.CAVE_ENTRACE);
        super.setShadow(ColorFilter.NOFILTER);
        super.situate(parentMap);
    }
}
