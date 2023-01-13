package summit.game.structure;

import summit.game.GameUpdateEvent;
import summit.game.animation.GlistenAnimation;
import summit.game.gamemap.DungeonsMap;
import summit.game.gamemap.GameMap;
import summit.gfx.ColorFilter;
import summit.gfx.RenderLayers;
import summit.gfx.Sprite;

/**
 * 
 * The DungeonsEntrance class is a MapEntrance that serves as the entrance to
 * the DungeonsMap.
 * 
 * It has the same properties as a MapEntrance but it has a different sprite and
 * shadow.
 * 
 * This class also overrides the update method to add a random glisten animation
 * effect, and
 * 
 * overrides the reinit method to reinitialize the DungeonMap.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R.
 */
public class DungeonsEntrance extends MapEntrance {

    /**
     * 
     * Creates a new instance of the DungeonsEntrance class.
     * 
     * @param x         the x position of the DungeonsEntrance on the map
     * 
     * @param y         the y position of the DungeonsEntrance on the map
     * 
     * @param parentMap the GameMap that the DungeonsEntrance is currently on
     */
    public DungeonsEntrance(float x, float y, GameMap parentMap) {
        super(x, y, 2, 2, new DungeonsMap(parentMap.getPlayer(), parentMap.getSeed()), parentMap);
        super.setShadow(ColorFilter.NOFILTER);

        super.getExMap().addStructure(new DungeonsExit(
                super.getExMap().getWidth() / 2 - 0.5f,
                super.getExMap().getHeight() / 2 + 2 - 0.5f,
                parentMap, super.getExMap()));

        super.setSprite(Sprite.DUNGEON_PORTAL);
        super.situate(parentMap);
    }

    /**
     * 
     * The update method is responsible for updating the state of the
     * DungeonsEntrance object.
     * In this implementation, the method randomly generates a "glisten" animation
     * on the DungeonsEntrance object
     * with a small probability.
     * 
     * @param e GameUpdateEvent object that contains information about the current
     *          game state.
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
     * 
     * The reinit() method of the DungeonsEntrance class is used to reset the state
     * of the object
     * and any related objects or variables. This method calls the reinit() method
     * of the super class
     * and also reinitializes the map stored in the exMap variable.
     */
    @Override
    public void reinit() {
        super.reinit();
        getExMap().reinit();
    }
}
