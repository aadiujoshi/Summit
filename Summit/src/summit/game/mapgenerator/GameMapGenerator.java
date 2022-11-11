package summit.game.mapgenerator;

import summit.game.GameMap;
import summit.game.tile.GrassTile;
import summit.game.tile.SnowTile;
import summit.game.tile.StoneTile;
import summit.game.tile.TileStack;
import summit.game.tile.WaterTile;

public class GameMapGenerator {
    
    private GameMapGenerator(){}

    public static GameMap generateStage1(final long seed){
        OpenSimplexNoise gen = new OpenSimplexNoise(seed);        
        GameMap map = new GameMap("stage1", seed, 128, 128);

        double[][] heightMap = new double[128][128];

        TileStack[][] tiles =  map.getMap();

        //-----------   init map   --------------------------------------------
        for (int x = 0; x < heightMap[0].length; x++) {
            for (int y = 0; y < heightMap.length; y++) {
                heightMap[y][x] = gen.eval((double)x/10, (double)y/10);
                tiles[y][x] = new TileStack(x, y);
                tiles[y][x].pushTile(new StoneTile(x, y));
            }
        }
        //---------------------------------------------------------------------
        


        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                double val = heightMap[i][j];
                TileStack t = tiles[i][j];
                if(val < 0){
                    t.pushTile(new StoneTile(j, i));
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

    public static GameMap generateStage2(final long seed){
        return null;
    }

    public static GameMap generateStage3(final long seed){
        return null;
    }

}
