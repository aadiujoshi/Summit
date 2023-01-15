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

/**
 * 
 * Class representing a Door object. Extends the MapEntrance class and is used
 * to transition between different maps.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R. S.
 */
public class Door extends MapEntrance {

    /**
     * 
     * Constructor for the Door class. Takes in the x and y coordinates for the
     * door,
     * the GameMap that the door leads to and the parent Map that the door is
     * located in.
     * 
     * @param x         the x-coordinate of the door
     * @param y         the y-coordinate of the door
     * @param exMap     the GameMap that the door leads to
     * @param parentMap the parent Map that the door is located in
     */
    public Door(float x, float y, GameMap exMap, GameMap parentMap) {
        super(x, y, 1, 2, exMap, parentMap);
        super.setSprite(Sprite.DOOR);
        super.setShadow(new ColorFilter(-50, -50, -50));
        super.setEnterOrientation(Direction.NORTH);
        super.situate(parentMap);
    }

}
