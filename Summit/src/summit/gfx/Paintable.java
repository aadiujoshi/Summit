/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.gfx;

public interface Paintable {
    /**
     * Used to set the layering for the paintable object. 10 total Layers, 0 -> 9.
     * <p>
     * RenderLayers to store 2D ArrayList to store layers
     */
    public void setRenderLayer(OrderPaintEvent e);

    /**
     * Called after the order method to render the Paintable object
     */
    public void paint(PaintEvent e);
}
