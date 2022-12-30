package summit.game.structure;

import java.awt.Point;

import summit.game.GameUpdateEvent;
import summit.game.gamemap.GameMap;
import summit.gfx.ColorFilter;
import summit.gfx.PaintEvent;
import summit.gfx.Renderer;
import summit.gfx.Sprite;
import summit.util.Direction;
 
public class BossRoomExit extends MapEntrance{

    public BossRoomExit(float x, float y, GameMap exMap, GameMap parentMap) {
        super(x, y, 2, 2, exMap, parentMap);
        super.setShadow(ColorFilter.NOFILTER);
        // super.setSpriteOffsetX(-0.5f);
        // super.setSpriteOffsetY(-0.5f);
        super.setSprite(Sprite.ICE_DOOR_NO_LOCKS);
        super.setEnterOrientation(Direction.SOUTH);

        super.situate(parentMap);
    }

    @Override
    public void reinit(){
    }
}
