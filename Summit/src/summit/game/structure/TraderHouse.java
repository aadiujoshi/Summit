/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.structure;

import summit.game.gamemap.GameMap;
import summit.game.gamemap.TraderHouseMap;
import summit.gfx.ColorFilter;
import summit.gfx.Sprite;

public class TraderHouse extends MapEntrance{
    
    public TraderHouse(float x, float y, GameMap parentMap){
        super(x, y, 4, 3, new TraderHouseMap(parentMap.getPlayer(), x, y, parentMap.getSeed()), parentMap);

        TraderHouseMap interior = (TraderHouseMap)super.getExMap();

        interior.addStructure(new Door(7, 0, parentMap, interior));

        super.setExMap(interior);

        super.setSprite(Sprite.VILLAGE_HOUSE);
        super.setSpriteOffsetY(0.5f);
    }
}