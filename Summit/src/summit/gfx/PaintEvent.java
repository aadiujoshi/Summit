package summit.gfx;

public class PaintEvent {
    private Renderer renderer;
    private long paintTime;
    private Camera camera;

    public PaintEvent(Renderer renderer, long paintTime, Camera camera){
        this.renderer = renderer;
        this.paintTime = paintTime;
        this.camera = camera;
    }

    public Renderer getRenderer(){
        return this.renderer;
    }

    public long getPaintTime(){
        return this.paintTime;
    }

    public Camera getCamera() {
        return this.camera;
    }
}
