/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.gfx;

public interface Paintable {
    /**
     * Used to set the layering for the paintable object. 10 total Layers, 0 to 9, which are rendered in order.
     * 
     * @param e an OrderPaintEvent object that provides the method to add a paintable instance to a RenderLayer
     */
    public void setRenderLayer(OrderPaintEvent e);

    /**
     * Render this object using the provided {@link PaintEvent} object
     * 
     * @param e A {@link PaintEvent} object
     */
    public void paint(PaintEvent e);
}
