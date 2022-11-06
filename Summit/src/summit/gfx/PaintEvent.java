package summit.gfx;

public class PaintEvent {
    private Renderer renderer;
    private long lastFrame;
    private Camera camera;

    public PaintEvent(Renderer renderer, long lastFrame, Camera camera){
        this.renderer = renderer;
        this.lastFrame = lastFrame;
        this.camera = camera;
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
}
