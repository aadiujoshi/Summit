package summit.game;

public class GameUpdateEvent{

    private GameMap map;
    private int deltaTime;
    private boolean tickInstance;

    public GameUpdateEvent(GameMap map, int deltaTime, boolean tickInstance){
        this.map = map;
        this.deltaTime = deltaTime;
        this.tickInstance = tickInstance;
    }
    
    public GameMap getMap() {
        return this.map;
    }
    
    public int getDeltaTime() {
        return this.deltaTime;
    }
    
    public boolean isTickInstance() {
        return this.tickInstance;
    }
}