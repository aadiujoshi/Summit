package summit.gfx;

public class OrderPaintEvent {
    
    private RenderLayers rl;
    private Camera cam;

    public OrderPaintEvent(RenderLayers rl, Camera camera){
        this.rl = rl;
        this.cam = camera;
    }

    public void addToLayer(int layer, Paintable p){
        rl.addToLayer(layer, p);
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
