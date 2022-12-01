package summit.gfx;

import java.io.Serializable;

public class ColorFilter implements Serializable{
    
    //jvm crashes if colorfilter is null for some reason
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

    public int filterColor(int color){
        
        int r1 = ((color >> 16) & 0xff);
        int g1 = ((color >> 8) & 0xff);
        int blue = ((color >> 0) & 0xff);
        
        r1 = ((r1+this.getRed() > 255) ? 255: r1+this.getRed());
        g1 = ((g1+this.getGreen() > 255) ? 255: g1+this.getGreen());
        blue = ((blue+this.getBlue() > 255) ? 255: blue+this.getBlue());
        
        r1 = ((r1 < 0) ? 0: r1);
        g1 = ((g1 < 0) ? 0: g1);
        blue = ((blue < 0) ? 0: blue);

        return (r1 << 16) | (g1 << 8) | (blue << 0);
    }

    //------- getters and setters ----------------------------

    @Override
    public String toString(){
        return "[" + red + "  " + green + "  " + blue + "]";
    }

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
