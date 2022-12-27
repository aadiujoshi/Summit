package summit.game.structure;

import summit.game.gamemap.BossRoom;
import summit.game.gamemap.DungeonsMap;
import summit.game.gamemap.GameMap;
import summit.gfx.ColorFilter;
import summit.gfx.Sprite;

public class DungeonsEntrace extends MapEntrance{
    
    /**
     * parentmap is the main map and exmap is a dungeon map
     */
    public DungeonsEntrace(float x, float y, GameMap parentMap) {
        // super(x, y, 1, 1, new DungeonsMap(parentMap.getPlayer(), parentMap.getSeed()), parentMap);
        super(x, y, 1, 1, new DungeonsMap(parentMap.getPlayer(), parentMap.getSeed()), parentMap);
        super.setShadow(ColorFilter.NOFILTER);

        super.getExMap().addStructure(new DungeonsExit( 
                            super.getExMap().getWidth()/2, 
                            super.getExMap().getHeight()/2+2, 
                            parentMap, super.getExMap()));

        super.setSprite(Sprite.CAVE_ENTRACE);
        super.situate(parentMap);
    }
}
