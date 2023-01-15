
package summit.game.structure;

import summit.game.GameUpdateEvent;
import summit.game.entity.mob.Player;
import summit.game.gamemap.GameMap;
import summit.util.Direction;

/**
 * 
 * The MapEntrance class is a Structure that serves as a way for the player to
 * enter and exit different maps in the game.
 * 
 * It has a width, height, and location on the parent map, and also contains a
 * reference to the GameMap that the player will enter.
 * 
 * This class also overrides the gameClick method to allow the player to
 * transition to the linked GameMap when clicked.
 * 
 * It also has a setEnterOrientation method that sets the direction that the
 * player is facing when entering the MapEntrance.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R. S.
 */
public class MapEntrance extends Structure {

    private GameMap exMap;

    private Direction enterOrientation = Direction.SOUTH;

    /**
     * 
     * Creates a new instance of the MapEntrance class.
     * 
     * @param x         the x position of the MapEntrance on the parent map
     * @param y         the y position of the MapEntrance on the parent map
     * @param width     the width of the MapEntrance
     * @param height    the height of the MapEntrance
     * @param exMap     the GameMap that the player will enter when interacting with
     *                  the MapEntrance
     * @param parentMap the GameMap that the MapEntrance is currently on
     */
    public MapEntrance(float x, float y, float width, float height, GameMap exMap, GameMap parentMap) {
        super(x, y, width, height, parentMap);
        this.exMap = exMap;
    }

    /**
     * 
     * The gameClick method is responsible for updating the state of the MapEntrance
     * object when it is clicked by the player.
     * 
     * In this implementation, the method allows the player to transition to the
     * linked GameMap when clicked.
     * 
     * @param e GameUpdateEvent object that contains information about the current
     *          game state.
     */
    @Override
    public void gameClick(GameUpdateEvent e) {
        Player p = e.getMap().getPlayer();

        if ((enterOrientation == Direction.SOUTH &&
                getY() - getHeight() / 2 > p.getY() && p.getX() < getX() + getWidth() / 2
                && p.getX() > getX() - getWidth() / 2) ||

                (enterOrientation == Direction.NORTH &&
                        getY() - getHeight() / 2 < p.getY() && p.getX() < getX() + getWidth() / 2
                        && p.getX() > getX() - getWidth() / 2)) {

            e.setLoadedMap(exMap);
        }
    }

    /**
     * 
     * @return the direction the player is facing when entering the MapEntrance
     */
    public Direction getEnterOrientation() {
        return enterOrientation;
    }

    /**
     * 
     * Sets the GameMap that the player will enter when interacting with the
     * MapEntrance
     * 
     * @param map the GameMap that the player will enter
     */
    public void setExMap(GameMap map) {
        this.exMap = map;
    }

    /**
     * 
     * @return the GameMap that the player will enter when interacting with the
     *         MapEntrance
     */
    public GameMap getExMap() {
        return this.exMap;
    }

    /**
     * Sets the direction that the player is facing when entering the MapEntrance
     * 
     * @param d the direction the player is facing when entering the MapEntrance
     */
    public void setEnterOrientation(Direction d) {
        this.enterOrientation = d;
    }
}
