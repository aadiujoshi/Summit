/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.structure;

import summit.game.GameUpdateEvent;
import summit.game.entity.mob.Player;
import summit.game.gamemap.GameMap;
import summit.gfx.ColorFilter;
import summit.gfx.Sprite;
import summit.util.Direction;

public class Door extends MapEntrance{

    /**
     * exMap is the "main" GameMap, and parentMap is the room/building the door is in
     * @param x
     * @param y
     * @param exMap
     * @param parentMap
     */
    public Door(float x, float y, GameMap exMap, GameMap parentMap) {
        super(x, y, 1, 2, exMap, parentMap);
        super.setSprite(Sprite.DOOR);
        super.setShadow(new ColorFilter(-50, -50, -50));
        super.setEnterOrientation(Direction.NORTH);
        super.situate(parentMap);
    }

}
