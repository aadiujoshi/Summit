package summit.game.mapgenerator;

import java.util.Random;

import summit.game.GameMap;
import summit.game.entity.mob.Player;
import summit.game.entity.mob.Skeleton;
import summit.game.entity.mob.Zombie;
import summit.game.structure.Door;
import summit.game.structure.TraderHouse;
import summit.game.structure.Tree;
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
        Random random = new Random(seed);
        GameMap map = new GameMap("stage1", seed, 128, 128);

        double[][] heightMap = new double[128][128];

        TileStack[][] tiles =  map.getTiles();

        for(int i = 0; i < tiles.length; i++){
            for(int j = 0; j < tiles[0].length; j++){
                tiles[i][j].pushTile(new StoneTile(j, i));
                heightMap[i][j] = gen.eval(j/10.0, i/10.0);
            }
        }

        //base tiles
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
                    if(val > 0.3)
                        t.pushTile(new SnowTile(j, i));
                }
            }
        }

        //trees!!!
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                double val = heightMap[i][j];
                
                if(val > 0.6){
                    if(random.nextInt(4) == 0)
                        map.spawn(new Tree(j, i));
                }
            }
        }

        // map.spawn(new Tree(40, 40));

        map.addStructure(new TraderHouse(19.5f, 19f, map));

        for (int i = 40; i < 41; i++) {
            for (int j = 40; j < 41; j++) {
                map.spawn(new Zombie(j, i));
            }
        }

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

        // tiles[2][2].pushTile(new GrassTile(2, 2));
        // tiles[3][2].pushTile(new GrassTile(3, 2));
        // tiles[2][3].pushTile(new GrassTile(2, 3));
        // tiles[3][3].pushTile(new GrassTile(3, 3));
        // tiles[3][3].pushTile(new GrassTile(3, 3));
        
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
