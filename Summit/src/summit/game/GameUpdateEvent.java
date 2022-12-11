/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game;

import summit.game.gamemap.GameMap;
import summit.gfx.Renderer;
import summit.gui.Window;
import summit.util.Time;

public class GameUpdateEvent{

    private Window window;

    private GameMap map;
    private GameWorld world;
    private int deltaTime;

    private int mouseX_pix;
    private int mouseY_pix;
    
    private float gameX;
    private float gameY;

    private long gametime;

    //simulate if mouse was clicked on this event instance
    private boolean mouseClicked;

    public GameUpdateEvent(GameWorld world, int deltaTime){
        this.map = world.getLoadedMap();
        this.world = world;
        this.window = world.getParentWindow();
        this.deltaTime = deltaTime;
        
        this.mouseX_pix = window.mouseX();
        this.mouseY_pix = window.mouseY();
        
        java.awt.geom.Point2D.Float p = Renderer.toTile(mouseX_pix, mouseY_pix, world.getCamera());

        this.gameX = p.x;
        this.gameY = p.y;

        this.mouseClicked = window.availableClick();
        this.gametime = world.getGametime();
    }
    
    public void setLoadedMap(GameMap m){
        world.setLoadedMap(m);
    }

    public GameMap getMap() {
        return this.map;
    }
    
    public long gameTime(){
        return this.gametime;
    }

    public int getDeltaTimeNS() {
        return this.deltaTime;
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

    public boolean mouseClicked(){
        return this.mouseClicked;
    }
}