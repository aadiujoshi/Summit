package summit.game.structure;

import java.awt.Point;

import summit.game.GameUpdateEvent;
import summit.game.gamemap.GameMap;
import summit.gfx.ColorFilter;
import summit.gfx.PaintEvent;
import summit.gfx.Renderer;
import summit.gfx.Sprite;
import summit.util.Direction;

/**
 * 
 * The BossRoomExit class is a MapEntrance that serves as the exit from the
 * BossRoom. It has the same properties as a MapEntrance but it has a different
 * sprite, shadow and enter orientation. This class also overrides the reinit()
 * method to do nothing.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R. S.
 */
public class BossRoomExit extends MapEntrance {

    /**
     * 
     * Creates a new instance of the BossRoomExit class.
     * 
     * @param x         the x position of the BossRoomExit on the map
     * 
     * @param y         the y position of the BossRoomExit on the map
     * 
     * @param exMap     the GameMap that the player will be transferred to when the
     *                  BossRoomExit is used
     * 
     * @param parentMap the GameMap that the BossRoomExit is currently on
     */
    public BossRoomExit(float x, float y, GameMap exMap, GameMap parentMap) {
        super(x, y, 2, 2, exMap, parentMap);
        super.setShadow(ColorFilter.NOFILTER);
        // super.setSpriteOffsetX(-0.5f);
        // super.setSpriteOffsetY(-0.5f);
        super.setSprite(Sprite.ICE_DOOR_NO_LOCKS);
        super.setEnterOrientation(Direction.SOUTH);

        super.situate(parentMap);
    }

    /**
     * 
     * This method does nothing as the BossRoomExit does not need to be
     * reinitialized.
     */
    @Override
    public void reinit() {
    }
}
