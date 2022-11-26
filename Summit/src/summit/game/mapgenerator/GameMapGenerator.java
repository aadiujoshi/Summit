package summit.game.mapgenerator;

import summit.game.GameMap;
import summit.game.entity.mob.Player;
import summit.game.entity.mob.Skeleton;
import summit.game.structure.Door;
import summit.game.structure.TraderHouse;
import summit.game.tile.GrassTile;
import summit.game.tile.LavaTile;
import summit.game.tile.SnowTile;
import summit.game.tile.StoneTile;
import summit.game.tile.TileStack;
import summit.game.tile.WaterTile;
import summit.game.tile.WoodPlank;
import summit.gfx.Camera;

public class GameMapGenerator {
    
    private GameMapGenerator(){}

    public static GameMap generateStage1(final long seed){
        OpenSimplexNoise gen = new OpenSimplexNoise(seed);        
        GameMap map = new GameMap("stage1", seed, 128, 128);

        double[][] heightMap = new double[128][128];

        TileStack[][] tiles =  map.getTiles();

        for(int i = 0; i < tiles.length; i++){
            for(int j = 0; j < tiles[0].length; j++){
                tiles[i][j].pushTile(new StoneTile(j, i));
                heightMap[i][j] = gen.eval(j/10.0, i/10.0);
            }
        }

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

        map.addStructure(new TraderHouse(19.5f, 19f, map));

        map.spawn(new Skeleton(35, 35));

        return map;
    }

    public static GameMap generateTraderHouse(GameMap parentMap){
        GameMap map = new GameMap("traderhouse", -1, 10, 10);
        
        TileStack[][] tiles = map.getTiles();

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                tiles[i][j].pushTile(new WoodPlank(j, i));
            }
        }
        
        map.addStructure(new Door(7f, 0, parentMap, map));
        map.setCamera(new Camera(7f, 1));
        return map;
    }

    public static GameMap generateStage2(final long seed){
        return null;
    }

    public static GameMap generateStage3(final long seed){
        return null;
    }

}
