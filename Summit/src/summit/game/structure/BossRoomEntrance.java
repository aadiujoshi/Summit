package summit.game.structure;

import java.awt.Point;

import summit.game.GameUpdateEvent;
import summit.game.gamemap.BossRoom;
import summit.game.gamemap.GameMap;
import summit.gfx.PaintEvent;
import summit.gfx.Renderer;
import summit.gfx.Sprite;

/**
 * 
 * The BossRoomEntrance class extends MapEntrance, representing the entrance to
 * the boss room in the game.
 * 
 * It is a 2x2 grid map object that players can interact with and enter the boss
 * room.
 * 
 * It overrides the gameClick() method to check if the player has all 3 keys
 * before allowing them to enter the boss room.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R.
 */
public class BossRoomEntrance extends MapEntrance {

    /**
     * 
     * Creates a new BossRoomEntrance object with the specified location, parent
     * map, and BossRoom object
     * 
     * @param x         the x-coordinate of the object in the map grid
     * 
     * @param y         the y-coordinate of the object in the map grid
     * 
     * @param parentMap the parent map of this object
     */
    public BossRoomEntrance(float x, float y, GameMap parentMap) {
        super(x, y, 2, 2, new BossRoom(parentMap.getPlayer(), parentMap.getSeed()), parentMap);
        super.setSprite(Sprite.ICE_DOOR);

        super.getExMap().addStructure(new BossRoomExit(19.5f, 30 - 7.5f, parentMap, super.getExMap()));
    }

    /**
     * 
     * Overrides the gameClick() method to check if the player has all 3 keys before
     * allowing them to enter the boss room.
     * 
     * @param e the GameUpdateEvent object that holds the state of the game
     */
    @Override
    public void gameClick(GameUpdateEvent e) {
        int obk = 0;
        for (boolean k : e.getMap().getPlayer().getObtainedKeys()) {
            if (k)
                obk++;
        }
        
        // player has all 3 keys
        if (obk == 3) {
            super.gameClick(e);
        }
    }

    /**
     * 
     * Reinitializes the BossRoomEntrance object and its associated BossRoom object
     */
    @Override
    public void reinit() {
        super.reinit();
        getExMap().reinit();
    }
}
