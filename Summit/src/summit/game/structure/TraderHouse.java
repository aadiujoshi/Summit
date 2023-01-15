package summit.game.structure;

import summit.game.gamemap.GameMap;
import summit.game.gamemap.TraderHouseMap;
import summit.gfx.ColorFilter;
import summit.gfx.Sprite;

/**
 * 
 * The TraderHouse class is a MapEntrance that serves as the entrance to the
 * 
 * TraderHouseMap.
 * 
 * This class extends the functionality of the MapEntrance class by creating a
 * new instance of the TraderHouseMap
 * 
 * and adding a door to the interior of the TraderHouseMap.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R. S.
 */
public class TraderHouse extends MapEntrance {

    /**
     * 
     * Creates a new instance of the TraderHouse class.
     * 
     * @param x         the x position of the TraderHouse on the map
     * 
     * @param y         the y position of the TraderHouse on the map
     * 
     * @param parentMap the GameMap that the TraderHouse is currently on
     */
    public TraderHouse(float x, float y, GameMap parentMap) {
        super(x, y, 4, 3, new TraderHouseMap(parentMap.getPlayer(), x, y, parentMap.getSeed()), parentMap);

        TraderHouseMap interior = (TraderHouseMap) super.getExMap();

        interior.addStructure(new Door(7, 0, parentMap, interior));

        super.setSprite(Sprite.VILLAGE_HOUSE);
        super.setSpriteOffsetY(0.5f);
    }
}