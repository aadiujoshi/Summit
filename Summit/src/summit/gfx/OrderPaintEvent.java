package summit.gfx;

public class OrderPaintEvent {
    
    private RenderLayers rl;
    private Camera cam;

    public OrderPaintEvent(RenderLayers rl, Camera camera){
        this.rl = rl;
        this.cam = camera;
    }

    public RenderLayers getRenderLayers() {
        return this.rl;
    }

    public Camera getCamera() {
        return this.cam;
    }

    public void setCamera(Camera cam) {
        this.cam = cam;
    }
}
