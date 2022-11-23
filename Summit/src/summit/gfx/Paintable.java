package summit.gfx;

public interface Paintable {
    /**
     * Used to set the layering for the paintable object. 10 total Layers, 0 -> 9.
     * <p>
     * RenderLayers to store 2D ArrayList to store layers
     */
    public void setRenderLayer(OrderPaintEvent ope);

    /**
     * Called after the order method to render the Paintable object
     */
    public void paint(PaintEvent e);
}
