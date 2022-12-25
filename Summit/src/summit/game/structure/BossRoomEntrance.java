package summit.game.structure;

import java.awt.Point;

import summit.game.GameUpdateEvent;
import summit.game.gamemap.BossRoom;
import summit.game.gamemap.GameMap;
import summit.gfx.PaintEvent;
import summit.gfx.Renderer;
import summit.gfx.Sprite;

public class BossRoomEntrance extends MapEntrance{

    public BossRoomEntrance(float x, float y, GameMap parentMap) {
        super(x, y, 2, 2, new BossRoom(parentMap.getPlayer(), parentMap.getSeed()), parentMap);
        super.setSprite(Sprite.ICE_DOOR);
        // super.setSpriteOffsetY(0.5f);

        super.getExMap().addStructure(new BossRoomExit(25, 25, parentMap, super.getExMap()));
    }

    @Override
    public void paint(PaintEvent e){
        Point p = Renderer.toPixel(getX(), getY(), e.getCamera());

        e.getRenderer().fillRect((int)(1+p.x-getWidth()/2*16), (int)(p.y-getHeight()/2*16), (int)(getWidth()*16), (int)(getHeight()*16), Renderer.toIntRGB(0, 255, 0));

        super.paint(e);
    }

    @Override
    public void gameClick(GameUpdateEvent e) {
        int obk = 0;
        for (boolean k : e.getMap().getPlayer().getObtainedKeys()) {
            if(k)
                obk++;
        }

        //player has all 3 keys
        if(obk == 3){
            super.gameClick(e);
        }
    }
}
