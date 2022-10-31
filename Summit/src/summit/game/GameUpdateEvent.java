package summit.game;

public class GameUpdateEvent{

    GameMap map;
    boolean tickInstance;

    public GameUpdateEvent(GameMap map, boolean tickInstance){
        this.map = map;
        this.tickInstance = tickInstance;
    }
}