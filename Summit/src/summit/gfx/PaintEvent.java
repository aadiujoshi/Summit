package summit.gfx;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import summit.gui.Window;

public class PaintEvent {

    private Renderer renderer;
    private long lastFrame;
    private Camera camera;
    private Window window;

    public PaintEvent(Renderer renderer, long lastFrame, Camera camera, Window window){
        this.renderer = renderer;
        this.lastFrame = lastFrame;
        this.camera = camera;
        this.window = window;
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
}
