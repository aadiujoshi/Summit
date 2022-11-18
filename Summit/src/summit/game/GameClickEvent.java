package summit.game;

public class GameClickEvent{
    private GameWorld world;
    private GameMap map;
    
    private float gameX;
    private float gameY;

    public GameClickEvent(GameWorld world, GameMap map, float gx, float gy){
        this.world = world;
        this.map = map;
        this.gameX = gx;
        this.gameY = gy;
    }

    public float gameX(){
        return gameX;
    }

    public float gameY(){
        return gameY;
    }

    public GameWorld getWorld(){
        return world;
    }

    public GameMap getMap(){
        return map;
    }
}