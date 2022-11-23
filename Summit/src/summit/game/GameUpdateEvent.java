package summit.game;

public class GameUpdateEvent{

    private GameMap map;
    private GameWorld world;
    private int deltaTime;
    private boolean tickInstance;

    private int mouseX_pix;
    private int mouseY_pix;
    
    public GameUpdateEvent(GameWorld world, int deltaTime, 
                                int mouseX_pix, int mouseY_pix, 
                                boolean tickInstance){
                                
        this.map = world.getLoadedMap();
        this.world = world;
        this.deltaTime = deltaTime;
        
        this.mouseX_pix = mouseX_pix;
        this.mouseY_pix = mouseY_pix;

        this.tickInstance = tickInstance;
    }
    
    public void setLoadedMap(GameMap m){
        world.setLoadedMap(m);
    }

    public GameMap getMap() {
        return this.map;
    }
    
    public int getDeltaTimeNS() {
        return this.deltaTime;
    }
    
    public boolean isTickInstance() {
        return this.tickInstance;
    }

    public int mouseX() {
        return this.mouseX_pix;
    }
    
    public int mouseY() {
        return this.mouseY_pix;
    }
    
    public GameWorld getWorld(){
        return this.world;
    }
}