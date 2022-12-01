package summit.game;

import summit.gfx.Renderer;
import summit.gui.Window;

public class GameUpdateEvent{

    private Window window;

    private GameMap map;
    private GameWorld world;
    private int deltaTime;
    private boolean tickInstance;

    private int mouseX_pix;
    private int mouseY_pix;
    
    private float gameX;
    private float gameY;

    public GameUpdateEvent(GameWorld world, int deltaTime, 
                                int mouseX_pix, int mouseY_pix, 
                                boolean tickInstance){
        this.map = world.getLoadedMap();
        this.world = world;
        this.window = world.getParentWindow();
        this.deltaTime = deltaTime;
        
        this.mouseX_pix = mouseX_pix;
        this.mouseY_pix = mouseY_pix;

        this.tickInstance = tickInstance;

        java.awt.geom.Point2D.Float p = Renderer.toTile(mouseX_pix, mouseY_pix, world.getCamera());

        this.gameX = p.x;
        this.gameY = p.y;
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
    
    public float gameX(){
        return gameX;
    }

    public float gameY(){
        return gameY;
    }

    public Window getWindow() {
        return this.window;
    }
}