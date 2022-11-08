package summit.game.mapgenerator;

import summit.game.GameMap;
import summit.game.tile.SnowTile;
import summit.game.tile.StoneTile;
import summit.game.tile.TileStack;

public class GameMapGenerator {
    
    private GameMapGenerator(){}

    public static GameMap generateStage1(final long seed){
        GameMap map = new GameMap("stage1", seed, 100, 100);

        TileStack[][] tiles =  map.getMap();

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                tiles[i][j] = new TileStack(j, i);
                tiles[i][j].pushTile(new StoneTile(j, i));
            }
        }

        return map;
    }

    public static GameMap generateStage2(final long seed){
        return null;
    }

    public static GameMap generateStage3(final long seed){
        return null;
    }

}
