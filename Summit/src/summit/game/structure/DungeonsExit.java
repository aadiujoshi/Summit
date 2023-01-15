package summit.game.structure;

import summit.game.GameUpdateEvent;
import summit.game.animation.GlistenAnimation;
import summit.game.gamemap.GameMap;
import summit.gfx.ColorFilter;
import summit.gfx.RenderLayers;
import summit.gfx.Sprite;
import summit.util.GraphicsScheduler;
import summit.util.ScheduledEvent;

/**
 * 
 * The DungeonsExit class is a MapEntrance that serves as the exit from the
 * DungeonsMap.
 * 
 * It has the same properties as a MapEntrance but it has a different sprite and
 * shadow.
 * 
 * This class also overrides the update method to add a random glisten animation
 * effect, and
 * 
 * overrides the reinit method to reinitialize the DungeonMap.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R. S.
 * 
 */
public class DungeonsExit extends MapEntrance {

    /**
     * 
     * Creates a new instance of the DungeonsExit class.
     * exmap is the mainmap and parentmap is the dungeonmap
     * 
     * @param x         the x position of the DungeonsExit on the map
     * 
     * @param y         the y position of the DungeonsExit on the map
     * 
     * @param exMap     the GameMap that the DungeonsExit leads to
     * 
     * @param parentMap the GameMap that the DungeonsExit is currently on
     */
    public DungeonsExit(float x, float y, GameMap exMap, GameMap parentMap) {
        super(x, y, 2, 2, exMap, parentMap);
        super.setSprite(Sprite.DUNGEON_PORTAL);
        super.setShadow(ColorFilter.NOFILTER);
        super.situate(parentMap);

        // GraphicsScheduler.registerEvent(new ScheduledEvent() {

        // });
    }

    /**
     * 
     * The update method is responsible for updating the state of the DungeonsExit
     * object.
     * 
     * In this implementation, the method randomly generates a "glisten" animation
     * 
     * on the DungeonsExit object with a small probability.
     * 
     * @param e GameUpdateEvent object that contains information about the current
     *          game state.
     * 
     * @throws Exception
     */
    @Override
    public void update(GameUpdateEvent e) throws Exception {
        super.update(e);

        if (Math.random() >= 0.992) {

            float ox = (float) (Math.random() * 2 - 1);
            float oy = (float) (Math.random() * 2 - 1);

            GlistenAnimation ga = new GlistenAnimation(getX() + ox, getY() + oy, 4, 0x2aa3e6);
            ga.setRLayer(RenderLayers.STRUCTURE_ENTITY_LAYER + 1);

            e.getMap().addAnimation(ga);
        }
    }

    /**
     * 
     * The reinit() method of the DungeonsExit class is used to reset the state of
     * the object
     * and reinitialize the DungeonMap.
     */
    @Override
    public void reinit() {
    }
}
