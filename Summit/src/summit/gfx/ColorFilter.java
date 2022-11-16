package summit.gfx;

public class ColorFilter {
    
    //jvm crashes if this is null for some reason
    public static final ColorFilter NOFILTER = new ColorFilter(0, 0, 0);

    private int red;
    private int green;
    private int blue;

    /**
     * Allows for negative values, as opposed to bitmasked rgb
     * @param red
     * @param green
     * @param blue
     */
    public ColorFilter(int red, int green, int blue){
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    //------- getters and setters ----------------------------

    public int getRed() {
        return this.red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getGreen() {
        return this.green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getBlue() {
        return this.blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }
}
