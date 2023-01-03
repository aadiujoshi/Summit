/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.gfx;

/**
 * Represents an object that can be painted onto a canvas or screen.
 *
 * This interface provides the methods {@link #setRenderLayer(OrderPaintEvent)}
 * and {@link #paint(PaintEvent)}, which allow the object to be added to a set
 * of render layers and painted in a specific order, respectively.
 *
 *@author Aadi J, Aditya B, Sanjay R, Aadithya R. S.
 */
public interface Paintable {
    /**
     * Sets the layering for the paintable object. There are 10 layers, numbered
     * from 0 to 9, which are rendered in order.
     *
     * @param e an {@link OrderPaintEvent} object that provides the method to add a
     *          paintable instance to a render layer
     */
    public void setRenderLayer(OrderPaintEvent e);

    /**
     * Renders this object using the provided {@link PaintEvent} object.
     *
     * @param e a {@link PaintEvent} object that provides the necessary information
     *          and methods for rendering the object
     */
    public void paint(PaintEvent e);
}
