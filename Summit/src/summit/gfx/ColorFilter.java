package summit.gfx;

public class ColorFilter {
    
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
        
        int r = ((color >> 16) & 0xff);
        int g = ((color >> 8) & 0xff);
        int b = ((color >> 0) & 0xff);
        
        // System.out.println(red + "  " + green + "  " + blue);

        r = ((r+this.red > 255) ? 255: r+this.red);
        g = ((g+this.green > 255) ? 255: g+this.green);
        b = ((b+this.blue > 255) ? 255: b+this.blue);

        int r1 = ((r < 0) ? 0: r);
        int g1 = ((g < 0) ? 0: g);
        int b1 = ((b < 0) ? 0: b);

        // System.out.println(r1 + "  " + g1 + "  " + b1);

        return ((r1 << 16) | (g1 << 8) | (b1 << 0));
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
