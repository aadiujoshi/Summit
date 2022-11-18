package summit.game.mapgenerator;

import summit.game.GameMap;
import summit.game.structure.TraderHouse;
import summit.game.tile.GrassTile;
import summit.game.tile.SnowTile;
import summit.game.tile.StoneTile;
import summit.game.tile.TileStack;
import summit.game.tile.WaterTile;
import summit.game.tile.WoodPlank;

public class GameMapGenerator {
    
    private GameMapGenerator(){}

    public static GameMap generateStage1(final long seed){
        OpenSimplexNoise gen = new OpenSimplexNoise(seed);        
        GameMap map = new GameMap("stage1", seed, 128, 128);

        double[][] heightMap = new double[128][128];

        TileStack[][] tiles =  map.getTiles();

        

        map.addStructure(new TraderHouse(19.5f, 19f));

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                double val = heightMap[i][j];
                TileStack t = tiles[i][j];
                if(val < 0){
                    if(val < -0.5){
                        t.pushTile(new WaterTile(j, i));
                    }

                } else if(val > 0){
                    t.pushTile(new GrassTile(j, i));
                    t.pushTile(new SnowTile(j, i));
                }
            }
        }

        return map;
    }

    public static GameMap generateTraderHouse(){
        GameMap map = new GameMap("traderhouse", -1, 10, 10);

        TileStack[][] tiles = map.getTiles();

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                tiles[i][j].pushTile(new WoodPlank(j, i));
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
