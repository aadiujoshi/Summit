package summit.game.gamemap;

import summit.game.entity.mob.Player;
import summit.game.tile.TileStack;
import summit.game.tile.WoodPlank;
import summit.gfx.Camera;

/**
 * 
 * This class represents the TraderHouseMap, which is a subtype of GameMap.
 * It has a specific layout and properties that are unique to the
 * TraderHouseMap.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R. S.
 */
public class TraderHouseMap extends GameMap {

    /*
     * x and y are house locations
     */

    /**
     * Constructor for creating a new TraderHouseMap.
     * 
     * @param player the player object that will be present in the map
     * @param x      the x-coordinate of the house location
     * @param y      the y-coordinate of the house location
     * @param seed   the seed used for generating the map
     */
    public TraderHouseMap(Player player, float x, float y, long seed) {
        super(player, seed, 10, 10);

        int width = 10;
        int height = 10;

        TileStack[][] tiles = super.getTiles();

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                tiles[i][j].pushTile(new WoodPlank(j, i));
            }
        }

        dontSpawnMobs();

        setCamera(new Camera(7f, 1f));
    }
}
