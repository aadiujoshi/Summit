package summit.game.mapgenerator;

import summit.game.GameMap;
import summit.game.tile.SnowTile;
import summit.game.tile.StoneTile;
import summit.game.tile.TileStack;

public class GameMapGenerator {
    
    private GameMapGenerator(){}

    public static GameMap generateStage1(final long seed){
        OpenSimplexNoise gen = new OpenSimplexNoise(seed);        
        GameMap map = new GameMap("stage1", seed, 128, 128);

        double[][] heightMap = new double[128][128];

        TileStack[][] tiles =  map.getMap();

        for (int x = 0; x < heightMap[0].length; x++) {
            for (int y = 0; y < heightMap.length; y++) {
                heightMap[y][x] = gen.eval(x/10, y/10);
                tiles[y][x] = new TileStack(x, y);
                tiles[y][x].pushTile(new SnowTile(x, y));
                tiles[y][x].pushTile(new StoneTile(x, y));
            }
        }



        // for (int i = 0; i < tiles.length; i++) {
        //     for (int j = 0; j < tiles[0].length; j++) {
        //         double val = heightMap[i][j];
        //         if(val < 0){
        //             tiles[i][j].pushTile(new StoneTile(j, i));
        //             if(val < -0.5){
        //                 til
        //             }

        //         } else if(val > 0){
        //             tiles[i][j].pushTile(new GrassTile(j, i));
        //             tiles[i][j].pushTile(new SnowTile(j, i));
        //         }
        //     }
        // }

        return map;
    }

    public static GameMap generateStage2(final long seed){
        return null;
    }

    public static GameMap generateStage3(final long seed){
        return null;
    }

}
