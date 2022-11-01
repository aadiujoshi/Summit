package summit.game.mapgenerator;

import summit.game.GameMap;

public class GameMapGenerator {
    
    private GameMapGenerator(){}

    public static GameMap generateStage1(final long seed){
        GameMap map = new GameMap("stage1", seed, 256, 256);

        map.getMap();



        return map;
    }

    public static GameMap generateStage2(final long seed){
        return null;
    }

    public static GameMap generateStage3(final long seed){
        return null;
    }

}
