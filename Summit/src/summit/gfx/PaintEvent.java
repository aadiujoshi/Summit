package summit.gfx;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import summit.game.GameMap;
import summit.gui.Window;

public class PaintEvent {

    private GameMap map;
    private Renderer renderer;
    private long lastFrame;
    private Camera camera;
    private Window window;

    private Stack<Paintable> renderLater;

    public PaintEvent(Renderer renderer, long lastFrame, Camera camera, GameMap cur_map, Window window){
        this.renderer = renderer;
        this.lastFrame = lastFrame;
        this.camera = camera;
        this.window = window;
        this.renderLater = new Stack<>();
        this.map = cur_map;
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

    /**
     * Only used by GameWorld to set the Camera and not make a new PaintEvent instance
     * @param cam
     */
    public void setCamera(Camera cam){
        this.camera = cam;
    }
    
    public Window getWindow() {
        return this.window;
    }

    public void renderLater(Paintable p){
        renderLater.push(p);
    }

    public void invokeDelayed(){
        for (Paintable paintable : renderLater) {
            paintable.paint(this);
        }
    }
    
    public GameMap getCurrentMap() {
        return this.map;
    }

    /**
     * Only used by GameWorld to set the current map
     * @param map
     */
    public void setCurrentMap(GameMap map) {
        this.map = map;
    }

}
