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

        super.getExMap().addStructure(new BossRoomExit(19.5f, 30-7.5f, parentMap, super.getExMap()));
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
