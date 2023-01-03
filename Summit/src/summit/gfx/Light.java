
package summit.gfx;

import java.io.Serializable;

/**
 * 
 * The {@code Light} class represents a light source in a game space. It can be
 * painted on the screen and has a shape, radius, and RGB values.
 * 
 * @author @ Aadi J, Aditya B, Sanjay R, Aadithya R. S.
 */
public class Light implements Serializable, Paintable {

    /**
     * Enum representing the shape of a {@code Light}.
     */
    public static enum Shape {
        CIRCLE, SQUARE
    }

    /**
     * The render layer of this {@code Light}.
     */
    private Light.Shape shape = Light.Shape.CIRCLE;

    private int rLayer = RenderLayers.TILE_LAYER + 1;

    /**
     * A constant representing a "no light" state. This {@code Light} cannot have
     * its position changed.
     */
    public static final Light NO_LIGHT = new Light(0, 0, 0, 0, 0, 0) {
        public void setX(float x) {
        }

        public void setY(float y) {
        }
    };

    /**
     * The radius of this {@code Light}.
     */
    private float radius;

    /**
     * The x coordinate of this {@code Light} in the game space.
     */
    private float x;

    /**
     * The y coordinate of this {@code Light} in the game space.
     */
    private float y;

    /**
     * The red value of this {@code Light}.
     */
    private int red;

    /**
     * The green value of this {@code Light}.
     */
    private int green;

    /**
     * The blue value of this {@code Light}.
     */
    private int blue;

    /**
     * Constructs a new {@code Light} object with the specified RGB values
     * 
     * @param x      the gamespace x coordinate
     * @param y      the gamespace y coordinate
     * @param radius the gamespace radius of this light
     * @param r      red value
     * @param g      green value
     * @param b      blue value
     */
    public Light(float x, float y, float radius, int r, int g, int b) {
        this.radius = radius;
        this.x = x;
        this.y = y;
        this.red = r;
        this.green = g;
        this.blue = b;
    }

    /**
     * Constructs a new {@code Light} object with the specified {@code ColorFilter}
     * 
     * @param x      the gamespace x coordinate
     * @param y      the gamespace y coordinate
     * @param radius the gamespace radius of this light
     * @param fitler a {@code ColorFilter} which contains the RGB values for the
     *               light
     */
    public Light(float x, float y, float radius, ColorFilter filter) {
        this(x, y, radius, filter.getRed(), filter.getGreen(), filter.getBlue());
    }

    /**
     * 
     * Sets the render layer of this {@code Light}.
     * 
     * @param r the {@code OrderPaintEvent} to add this {@code Light} to
     */
    @Override
    public void setRenderLayer(OrderPaintEvent r) {
        r.addToLayer(rLayer, this);
    }

    /**
     * 
     * Paints this {@code Light} onto the screen.
     * 
     * @param e the {@code PaintEvent} containing the {@code Renderer} and
     *          {@code Camera} to use for painting
     */
    @Override
    public void paint(PaintEvent e) {
        e.getRenderer().renderLight(this, e.getCamera());
    }

    // ------------- getters and setters -------------------

    /**
     * 
     * Returns a string representation of this {@code Light}, including its
     * position, radius, and RGB values.
     * 
     * @return a string representation of this {@code Light}
     */
    @Override
    public String toString() {
        return "x: " + x + " y: " + y + " radius: " + radius + "  [" + red + "  " + green + "  " + blue + "]";
    }

    /**
     * 
     * Sets the shape of this {@code Light}.
     * 
     * @param s the {@code Shape} to set for this {@code Light}
     */
    public void setShape(Light.Shape s) {
        this.shape = s;
    }

    /**
     * 
     * Returns the shape of this {@code Light}.
     * 
     * @return the shape of this {@code Light}
     */
    public Light.Shape getShape() {
        return this.shape;
    }

    /**
     * 
     * Returns the radius of this {@code Light}.
     * 
     * @return the radius of this {@code Light}
     */
    public float getRadius() {
        return this.radius;
    }

    /**
     * 
     * Sets the radius of this {@code Light}.
     * 
     * @param radius the radius to set for this {@code Light}
     */
    public void setRadius(float radius) {
        this.radius = radius;
    }

    /**
     * 
     * Returns the x coordinate of this {@code Light} in the game space.
     * 
     * @return the x coordinate of this {@code Light}
     */
    public float getX() {
        return this.x;
    }

    /**
     * 
     * Sets the x coordinate of this {@code Light} in the game space.
     * 
     * @param x the x coordinate to set for this {@code Light}
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * 
     * Returns the y coordinate of this {@code Light} in the game space.
     * 
     * @return the y coordinate of this {@code Light}
     */
    public float getY() {
        return this.y;
    }

    /**
     * 
     * Sets the y coordinate of this {@code Light} in the game space.
     * 
     * @param y the y coordinate to set for this {@code Light}
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * 
     * Returns the red value of this {@code Light}.
     * 
     * @return the red value of this {@code Light}
     */
    public int getRed() {
        return this.red;
    }

    /**
     * 
     * Sets the red value of this {@code Light}.
     * 
     * @param red the red value to set for this {@code Light}
     */
    public void setRed(int red) {
        this.red = red;
    }

    /**
     * 
     * Returns the green value of this {@code Light}.
     * 
     * @return the green value of this {@code Light}
     */
    public int getGreen() {
        return this.green;
    }

    /**
     * 
     * Sets the green value of this {@code Light}.
     * 
     * @param green the green value to set for this {@code Light}
     */
    public void setGreen(int green) {
        this.green = green;
    }

    /**
     * 
     * Returns the blue value of this {@code Light}.
     * 
     * @return the blue value of this {@code Light}
     */
    public int getBlue() {
        return this.blue;
    }

    /**
     * 
     * Sets the blue value of this {@code Light}.
     * 
     * @param blue the blue value to set for this {@code Light}
     */
    public void setBlue(int blue) {
        this.blue = blue;
    }

    /**
     * Sets the render layer of this {@code Light}.
     * 
     * @param layer the render layer to set for this {@code Light}
     */
    public void setRenderLayer(int layer) {
        this.rLayer = layer;
    }

    /**
     * 
     * Returns a {@code ColorFilter} object containing the RGB values of this
     * {@code Light}.
     * 
     * @return a {@code ColorFilter} object representing the color of this
     *         {@code Light}
     */
    public ColorFilter getColorFilter() {
        return new ColorFilter(red, green, blue);
    }
}
