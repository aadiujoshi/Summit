/*
* BPA project by Aadi Joshi, Aditya Bhattaharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.gfx;

import summit.game.GameMap;
import summit.game.GameWorld;
import summit.gui.Window;

public class PaintEvent {

    private GameWorld world;
    private GameMap curMap;

    private Renderer renderer;
    private long lastFrame;
    private Camera camera;
    private Window window;

    private int mouseX_pix;
    private int mouseY_pix;
    
    private float gameX;
    private float gameY;

    public PaintEvent(GameWorld world, Window window, Renderer renderer, long lastFrame, int mouseXpix, int mouseYpix){
        this.renderer = renderer;
        this.lastFrame = lastFrame;
        this.window = window;

        this.mouseX_pix = mouseXpix;
        this.mouseY_pix = mouseYpix;

        if(world != null){
            this.camera = world.getCamera().clone();
            this.curMap = world.getLoadedMap();

            java.awt.geom.Point2D.Float p = Renderer.toTile(mouseX_pix, mouseY_pix, world.getCamera());

            this.gameX = p.x;
            this.gameY = p.y;
        }
    }

    public Renderer getRenderer(){
        return this.renderer;
    }

    public long getLastFrame(){
        return this.lastFrame;
    }

    public Camera getCamera() {
        return this.camera;
    }

    public Window getWindow() {
        return this.window;
    }

    public int mouseX() {
        return this.mouseX_pix;
    }
    
    public int mouseY() {
        return this.mouseY_pix;
    }
    
    public float gameX(){
        return gameX;
    }

    public float gameY(){
        return gameY;
    }
    
    public GameWorld getWorld() {
        return this.world;
    }
    
    public GameMap getLoadedMap() {
        return this.curMap;
    }
}
