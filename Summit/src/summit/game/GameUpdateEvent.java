package summit.game;

public class GameUpdateEvent{

    private GameMap map;
    private GameWorld world;
    private int deltaTime;
    private boolean tickInstance;

    private int mouseX_pix;
    private int mouseY_pix;

    public GameUpdateEvent(GameMap map, int deltaTime, 
                                int mouseX_pix, int mouseY_pix, 
                                boolean tickInstance){
        this.map = map;
        this.deltaTime = deltaTime;
        
        this.mouseX_pix = mouseX_pix;
        this.mouseY_pix = mouseY_pix;

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

    public int getMouseXpixel() {
        return this.mouseX_pix;
    }
    
    public int getMouseYpixel() {
        return this.mouseY_pix;
    }

    
    public GameWorld getWorld(){
        return this.world;
    }
}