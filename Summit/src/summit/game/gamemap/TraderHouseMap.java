package summit.game.gamemap;

import summit.game.structure.TraderHouse;
import summit.game.tile.TileStack;
import summit.game.tile.WoodPlank;
import summit.gfx.Camera;

public class TraderHouseMap extends GameMap{

    public TraderHouseMap(TraderHouse house, long seed) {
        super(seed, 10, 10);

        int width = 10;
        int height = 10;

        TileStack[][] tiles = super.getTiles();

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                tiles[i][j].pushTile(new WoodPlank(j, i));
            }
        }

        setCamera(new Camera(7f, 1f));
    }
}
