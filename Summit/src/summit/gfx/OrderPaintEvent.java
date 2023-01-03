
package summit.gfx;

/**
 * Represents an event that orders the painting of {@link Paintable} objects
 * onto a set of render layers, using a {@link Camera} to determine the order.
 *
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R. S.
 */
public class OrderPaintEvent {

    /**
     * The {@link RenderLayers} object that stores the layers of {@link Paintable}
     * objects.
     */
    private RenderLayers rl;

    /** The {@link Camera} object used to determine the order of painting. */
    private Camera cam;

    /**
     * Constructs an {@code OrderPaintEvent} object with the specified
     * {@link RenderLayers} and {@link Camera}.
     *
     * @param rl     the {@link RenderLayers} object that stores the layers of
     *               {@link Paintable} objects
     * @param camera the {@link Camera} object used to determine the order of
     *               painting
     */
    public OrderPaintEvent(RenderLayers rl, Camera camera) {
        this.rl = rl;
        this.cam = camera;
    }

    /**
     * Adds the specified {@link Paintable} object to the specified layer in the
     * {@link RenderLayers} object.
     *
     * @param layer the index of the layer to add the {@link Paintable} object to
     * @param p     the {@link Paintable} object to add to the layer
     */
    public void addToLayer(int layer, Paintable p) {
        rl.addToLayer(layer, p);
    }

    /**
     * Returns the {@link RenderLayers} object that stores the layers of
     * {@link Paintable} objects.
     *
     * @return the {@link RenderLayers} object
     */
    public RenderLayers getRenderLayers() {
        return this.rl;
    }

    /**
     * Returns the {@link Camera} object used to determine the order of painting.
     *
     * @return the {@link Camera} object
     */
    public Camera getCamera() {
        return this.cam;
    }

    /**
     * Sets the {@link Camera} object used to determine the order of painting.
     *
     * @param cam the {@link Camera} object to set
     */
    public void setCamera(Camera cam) {
        this.cam = cam;
    }
}
