package summit.gfx;

import java.io.Serializable;

/**
 * The ColorFilter class represents a color filter that can be applied to an RGB
 * color value. It implements the
 * Serializable interface to allow for saving and loading of color filter
 * objects.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R. S.
 */
public class ColorFilter implements Serializable {

    // jvm crashes if colorfilter is null for some reason

    /**
     * A ColorFilter object with all color channels set to 0.
     */
    public static final ColorFilter NOFILTER = new ColorFilter(0, 0, 0);

    /** The red channel of the color filter. */
    private int red;

    /** The green channel of the color filter. */
    private int green;

    /** The blue channel of the color filter. */
    private int blue;

    /**
     * Constructs a ColorFilter object with the specified red, green, and blue
     * channels. Allows for negative values,
     * as opposed to bitmasked RGB values.
     * 
     * @param red   the red channel of the color filter
     * @param green the green channel of the color filter
     * @param blue  the blue channel of the color filter
     */
    public ColorFilter(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public ColorFilter(int color) {
        this.red = ((color >> 16) & 0xff);
        this.green = ((color >> 8) & 0xff);
        this.blue = ((color >> 0) & 0xff);
    }

    /**
     * Applies the color filter to the specified RGB color value.
     *
     * @param color the RGB color value to apply the color filter to
     * @return the RGB color value with the color filter applied
     */
    public int filterColor(int color) {

        int r1 = ((color >> 16) & 0xff);
        int g1 = ((color >> 8) & 0xff);
        int blue = ((color >> 0) & 0xff);

        r1 = ((r1 + this.getRed() > 255) ? 255 : r1 + this.getRed());
        g1 = ((g1 + this.getGreen() > 255) ? 255 : g1 + this.getGreen());
        blue = ((blue + this.getBlue() > 255) ? 255 : blue + this.getBlue());

        r1 = ((r1 < 0) ? 0 : r1);
        g1 = ((g1 < 0) ? 0 : g1);
        blue = ((blue < 0) ? 0 : blue);

        return (r1 << 16) | (g1 << 8) | (blue << 0);
    }

    // ------- getters and setters ----------------------------

    /**
     * Returns a string representation of the ColorFilter object, in the format
     * "[red green blue]".
     * 
     * @return a string representation of the ColorFilter object
     */
    @Override
    public String toString() {
        return "[" + red + "  " + green + "  " + blue + "]";
    }

    /**
     * Returns the red channel of the color filter.
     * 
     * @return the red channel of the color filter
     */
    public int getRed() {
        return this.red;
    }

    /**
     * Sets the red channel of the color filter.
     * 
     * @param red the new red channel of the color filter
     */
    public void setRed(int red) {
        this.red = red;
    }

    /**
     * Returns the green channel of the color filter.
     * 
     * @return the green channel of the color filter
     */
    public int getGreen() {
        return this.green;
    }

    /**
     * Sets the green channel of the color filter.
     * 
     * @param green the new green channel of the color filter
     */
    public void setGreen(int green) {
        this.green = green;
    }

    /**
     * Returns the blue channel of the color filter.
     * 
     * @return the blue channel of the color filter
     */
    public int getBlue() {
        return this.blue;
    }

    /**
     * Sets the blue channel of the color filter.
     *
     * @param blue the new blue channel of the color filter
     */
    public void setBlue(int blue) {
        this.blue = blue;
    }
}