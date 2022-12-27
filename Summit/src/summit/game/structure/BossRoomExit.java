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

    // @Override
    // public void paint(PaintEvent e){
    //     Point p = Renderer.toPixel(getX(), getY(), e.getCamera());

    //     e.getRenderer().fillRect((int)(1+p.x-getWidth()/2*16), (int)(p.y-getHeight()/2*16), (int)(getWidth()*16), (int)(getHeight()*16), Renderer.toIntRGB(0, 255, 0));

    //     super.paint(e);
    // }
}
