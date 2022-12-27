/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.gfx;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.Serializable;

import summit.util.Region;
import summit.util.Settings;
import summit.util.Time;

public class Renderer implements Serializable{

    private int[][] frame;

    private static volatile boolean fullscreen;
    
    //--  used by final frame writer threads  --
    private final Writer[] writers;
    private int[] finalFrame;
    private final int finalHeight;
    private final int finalWidth;

    private int vsync = 0;
    //------------------------------------------

    public static final int WIDTH = 256;
    public static final int HEIGHT = 144;

    public static final int NO_OP = 0b0;
    public static final int FLIP_X = 0b1;
    public static final int FLIP_Y = 0b10;
    public static final int ROTATE_90 = 0b100;

    //Possible color combinations: Red, Yellow, Green, Cyan, Blue, Magenta, White, Black
    public static final int OUTLINE_RED = 0b1000;
    public static final int OUTLINE_GREEN = 0b10000;
    public static final int OUTLINE_BLUE = 0b100000;
    

    public Renderer(int t, int fWidth, int fHeight){
        frame = new int[HEIGHT][WIDTH];
        this.finalWidth = fWidth;
        this.finalHeight = fHeight;

        //--------------------------------------------------------------------------
        //concurrent rendering 
        //--------------------------------------------------------------------------

        if(t == 1){
            this.writers = null;
            return;
        }

        this.writers = new Writer[Renderer.getClosestFactor(t, fHeight)];
        
        for (int i = 0; i < writers.length; i++) {
            final int start = i*(fHeight/t);
            final int end = (i+1)*(fHeight/t);
            
            writers[i] = new Writer(start, end, fWidth, fHeight);
        }

        for (Thread wr : writers) {
            wr.setPriority(Thread.NORM_PRIORITY);
            wr.start();
        }
        //---------------------------------------------------------------------------------------
    }

    public void terminate(){
        if(writers == null) return;

        for (int i = 0; i < writers.length; i++) {
            writers[i].terminate();
        }
    }

    public void resetFrame(){
        frame = new int[HEIGHT][WIDTH]; 
    }

    //parallelize this process for more frames 🤑🤑🤑🤑
    public void upscaleToImage(BufferedImage newFrame){

        finalFrame = ((DataBufferInt)newFrame.getRaster().getDataBuffer()).getData();
        
        //if set to write with just one thread
        if(writers == null){
            float scaleX = finalWidth/WIDTH;
            float scaleY = finalHeight/HEIGHT;
            
            for(int r = 0; r < finalHeight; r++) {
                for(int c = 0; c < finalWidth; c++){
                    if(Math.round(r/scaleY) < frame.length && Math.round(c/scaleX) < frame[0].length){
                        finalFrame[r*finalWidth+c] = frame[Math.round(r/scaleY)][Math.round(c/scaleX)];
                    }
                }
            }
        }

        if(writers != null){
            //signal threads to start upscaling and writing to final frame array
            for (int i = 0; i < writers.length; i++) {
                writers[i].startProcess(finalFrame, frame);
            }

            Time.nanoDelay(Time.NS_IN_MS*(int)Settings.getSetting("vsync"));
        }
    }


    @Deprecated
    /**
     * does not work
     * @param scale
     */
    public void contrastFrame(float scale){
        int min = rgbIntensity(frame[0][0]);
        int max = min;
        int avg = (max+min)/2;

        for (int r = 0; r < frame.length; r++) {
            for (int c = 0; c < frame[0].length; c++) {
                int intensity = rgbIntensity(frame[r][c]);

                max = intensity > max ? intensity : max;
                min = intensity < min ? intensity : min;
            }
        }

        for (int r = 0; r < frame.length; r++) {
            for (int c = 0; c < frame[0].length; c++) {
                int ints = rgbIntensity(frame[r][c]);

                int rr = (frame[r][c] >> 16) & 0xff;
                int gg = (frame[r][c] >> 8) & 0xff;
                int bb = (frame[r][c] >> 0) & 0xff;

                if(ints < avg){
                    int negint = (int)((ints-min)*scale);
                    frame[r][c] = toIntRGB( rr-negint , gg-negint, bb-negint);
                } else {
                    int posint = (int)((max-ints)*scale);
                    frame[r][c] = toIntRGB( r+posint , gg+posint, bb+posint);
                }
            }
        }
    }


    /**
     * x and y are game coordinates
     * @param light
     * @param x
     * @param y
     */
    public void renderLight(Light light, Camera cam){
        if(light == null || cam == null)
            return;

        Point center = toPixel(light.getX(), light.getY(), cam);
        
        Light.Shape shape = light.getShape();

        float radius = light.getRadius()*16;

        //for square lights
        float sqrVertexDist = light.getRadius()*16*1.41421356f;

        int r = light.getRed();
        int b = light.getBlue();
        int g = light.getGreen();

        for(int xx = (int)(center.x-radius); xx < (int)(center.x+radius); xx++){
            for (int yy = (int)(center.y-radius); yy < (int)(center.y+radius); yy++) {
                if(!inArrBounds(yy, xx, frame.length, frame[0].length))
                    continue;
                float d = Region.distance(center.x, center.y, xx, yy);

                if(shape == Light.Shape.CIRCLE && d <= radius){
                    ColorFilter filt = new ColorFilter((int)(r-((d/radius)*r)), (int)(g-((d/radius)*g)), (int)(b-((d/radius)*b)));
                    frame[yy][xx] = filt.filterColor(frame[yy][xx]);
                }
            }
        }
    }

    /**
     * val is brightness (make negative to dim frame)
     * @param color
     */
    public void filterRect(int x, int y, int width, int height, ColorFilter filter){
        if(filter == null)
            return;
        
        for (int r = y; r < frame.length && r < y+height; r++) {
            for (int c = x; c < frame[0].length && c < x+width; c++) {
                if(inArrBounds(r, c, frame.length, frame[0].length))
                    frame[r][c] = filter.filterColor(frame[r][c]);
            }
        }
    }

    public void render(String sprite, int x, int y, int operation, ColorFilter filter){
        this.render(BufferedSprites.getSprite(sprite), x, y, operation, filter);
    }

    /**
     * USE THIS METHOD FOR GENERAL RENDERING (MENUS, DIALOGUE, ETC). COORDINATES ARE SCREEN COORDINATES
     */
    public void render(int[][] sprite, int x, int y, int operation, ColorFilter filter){

        if(sprite == null) return;

        int nx = Math.round(x-(sprite[0].length/2))+1;
        int ny = Math.round(y-(sprite.length/2));

        if(operation != NO_OP){
            if((operation & ROTATE_90) == ROTATE_90){
                sprite = rotate(sprite);
            }
            if((operation & FLIP_X) == FLIP_X){
                sprite = flip(sprite, FLIP_X);
            }
            if((operation & FLIP_Y) == FLIP_Y){
                sprite = flip(sprite, FLIP_Y);
            }
        }       

        if((operation >> 3) != 0){
            sprite = outline(sprite, operation >> 3);
        }
        
        //write final sprite
        for(int yy = ny; yy < ny+sprite.length; yy++) {
            for(int xx = nx; xx < nx+sprite[0].length; xx++) {
                if(inArrBounds(yy-ny, xx-nx, sprite.length, sprite[0].length) && inArrBounds(yy, xx, frame.length, frame[0].length) && validRGB(sprite[yy-ny][xx-nx])){
                    if(filter == null)
                        frame[yy][xx] = sprite[yy-ny][xx-nx];
                    else
                        frame[yy][xx] = filter.filterColor(sprite[yy-ny][xx-nx]); //Renderer.filterColor(sprite[yy-ny][xx-nx], filter);
                }
            }
        }
    }

    public void renderText(String text, int x, int y, int operation, ColorFilter filter){

        text = text.toUpperCase();

        int offsetX = x-(text.length()*8/2) + 4;

        for(int i = 0; i < text.length(); i++) {
            if(!(text.charAt(i)+"").equals(" "))
                render(text.charAt(i)+Sprite.TEXT_APPEND_KEY, offsetX+(i*8), y, operation, filter);
        }
    }

    /** 
     * USE THIS METHOD FOR RENDERING GAME STUFF (ANYTHING THAT IS POSITIONALLY BASED ON A CAMERA).
     * COORDINATES ARE GAMESPACE COORDINATES.
    */
    public void renderGame(String s, float x, float y, int operation, ColorFilter filter, Camera camera){

        Point spritePos = toPixel(x, y, camera);
        
        this.render(s, spritePos.x, spritePos.y, operation, filter);
    }

    /**
     * x and y are center coordinates
     * 
     * @param img
     * @param x
     * @param y
     */
    public void renderImage(BufferedImage img, int x, int y){
        for (int xx = x-(img.getWidth()/2); xx < x+(img.getWidth()/2); xx++) {
            for (int yy = y-(img.getHeight()/2); yy < y+(img.getHeight()/2); yy++) {
                int rgb = img.getRGB(xx - (x-(img.getWidth()/2)), yy - (y-(img.getHeight()/2)));
                if(inArrBounds(yy, xx, frame.length, frame[0].length) && validRGB(rgb))
                    frame[yy][xx] = rgb;
            }
        }
    }

    /**
     * x and y are top left coordinates
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public void fillRect(int x, int y, int width, int height, int color){
        for(int x1 = x; x1 < x+width; x1++){
            for(int y1 = y; y1 < y+height; y1++){
                if(inArrBounds(y1, x1, frame.length, frame[0].length))
                    frame[y1][x1] = color;
            }
        }
    }

    public int[] frameAsArray(){
        int[] reformatted = new int[frame.length*frame[0].length];

        //holy shit that works #program_in_c
        for(int row = 0; row < frame.length; row++) {
            System.arraycopy( frame[row],0 ,reformatted ,row*frame[0].length ,frame[0].length);
        }

        return reformatted;
    }

    public void drawLine(int sx, int sy, int ex, int ey, int color, boolean dotted){
        if(sx < 0 || ex < 0 || sy < 0 || ey < 0)
            return;
        if(sx > WIDTH || ex > WIDTH || sy > HEIGHT || ey > HEIGHT)
            return;

        if(sx == sy){
            int start = Math.min(sy, ey);
            int end = Math.max(sy, ey);

            for(int y = start; y <= end; y++){
                if(dotted && y % 5 != 0)
                    frame[y][sx] = color;
            }

            return;
        }

        float m = (float)(ey-sy) / (float)(ex-sx);
        int b = (int)(-1*(m*sx-sy));

        int start = Math.min(sx, ex);
        int end = Math.max(sx, ex);

        for(float inc_x = start; inc_x < end; inc_x += (1f/WIDTH)){
            if(dotted && Math.round(inc_x) % 5 != 0)
                frame[(int)(inc_x*m + b)][Math.round(inc_x)] = color;
        }
    }

    //--------------------------------------------------------------------
    // utility methods
    //--------------------------------------------------------------------

    //just for thread
    private static int getClosestFactor(int target, int number) {
        for (int i = 0; i < number; i++) {
            if (number % (target + i) == 0) {
                return target + i;
            } else if (number % (target - i) == 0) {
                return target - i;
            }
        }
        return number;
    }

    public static int toIntRGB(int r, int g, int b){
        return ((r > 255) ? 255 : r << 16) | 
                ((g > 255) ? 255 : g << 8) | 
                ((b > 255) ? 255 : b);
    }

    public static boolean validRGB(int rgb){
        return rgb != -1;
    }

    public static int rgbIntensity(int color){
        return (((color >> 16) & 0xff) + 
                ((color >> 8) & 0xff) + 
                ((color >> 0) & 0xff))/3;
    }

    /**
     * Camera is left in gamecoordinates
     */
    public static java.awt.Point toPixel(float x, float y, Camera cam){
        float nx = (WIDTH/2)+(x*16F)-(cam.getX()*16F);
        float ny = (HEIGHT/2)-(y*16F)+(cam.getY()*16F);

        return new java.awt.Point(Math.round(nx), Math.round(ny));
    }

    /**
     * mx and my are mouse coordinates pre-converted to renderer pixel space (256x144)
     * @param mx
     * @param my
     * @param cam
     * @return
     */
    public static Point2D.Float toTile(int mx, int my, Camera cam){
        float rx = (cam.getX() - ((WIDTH/16f)/2)) + (mx/16f);// - 0.1f;
        float ry = (cam.getY() + ((HEIGHT/16f)/2)) - (my/16f) + (fullscreen ? 0 : 0.25f);

        return new Point2D.Float(rx, ry);
    }

    /** 
     * Rotate 90 degrees counterclockwise
    */
    public static int[][] rotate(int[][] arr){
        int[][] rotated = new int[arr.length][arr[0].length];

        for(int r = 0; r < arr.length; r++) {
            for(int c = 0; c < arr[0].length; c++) {
                //transform matrix
                //prev column index becomes row index
                //arr[0].length-r-1 for new column index

                rotated[c][arr[0].length-r-1] = arr[r][c];
            }
        }

        return arr = rotated;
    }

    public static int[][] flip(int[][] arr, int transform){
        int[][] transformed = new int[arr.length][arr[0].length];

        if((transform & FLIP_X) == FLIP_X){
            for(int r = 0; r < arr.length; r++){
                for(int c = 0; c < arr[0].length/2+1; c++){
                    transformed[r][c] = arr[r][arr[0].length-c-1];
                    transformed[r][arr[0].length-c-1] = arr[r][c];
                }
            }
        }
        if((transform & FLIP_Y) == FLIP_Y){
            for(int r = 0; r < arr.length/2+1; r++){
                for(int c = 0; c < arr[0].length; c++){
                    transformed[r][c] = arr[arr.length-r-1][c];
                    transformed[arr.length-r-1][c] = arr[r][c];
                }
            }
        }

        return arr = transformed;
    }

    /**
     * outline an image, leave flag packed 
     * @param s
     * @param color
     * @return
     */
    public static int[][] outline(int[][] sprite, int flag){

        //unpack flag
        flag <<= 3;
        int color =  ((flag & OUTLINE_RED) == OUTLINE_RED ? 0xff << 16 : 0)
                        | ((flag & OUTLINE_GREEN) == OUTLINE_GREEN ? 0xff << 8 : 0)
                        | ((flag & OUTLINE_BLUE) == OUTLINE_BLUE ? 0xff << 0 : 0);

        int[][] outlined = new int[sprite.length][sprite[0].length];

        for(int r = 0; r < sprite.length; r++){
            for(int c = 0; c < sprite[0].length; c++){
                if(sprite[r][c] != -1){
                    if( //check if on edge of sprite and is valid rgb 
                        
                        (r == 0) ||
                        (r == sprite.length-1) ||
                        (c == 0) ||
                        (c == sprite[0].length-1) ||

                        //check sides
                        (r-1 > -1 && sprite[r-1][c] == -1) ||
                        (c-1 > -1 && sprite[r][c-1] == -1) ||
                        (r+1 < sprite.length && sprite[r+1][c] == -1) ||
                        (c+1 < sprite[0].length && sprite[r][c+1] == -1) || 
                        
                        //check corners
                        (r-1 > -1 && c-1 > -1 && sprite[r-1][c-1] == -1) || 
                        (r-1 > -1 && c+1 < sprite[0].length && sprite[r-1][c+1] == -1) ||
                        (r+1 < sprite.length && c-1 > -1 && sprite[r+1][c-1] == -1) || 
                        (r+1 < sprite.length && c+1 < sprite[0].length && sprite[r+1][c+1] == -1)){
                        
                        outlined[r][c] = color;
                    } else {
                        outlined[r][c] = sprite[r][c];
                    }
                } else {
                    outlined[r][c] = sprite[r][c];
                }
            }
        }

        return outlined;
    }

    public static boolean inArrBounds(int r, int c, int rows, int cols){
        return (r >= 0) && (r < rows) && (c >= 0) && (c < cols);
    }

    //------------------------------------------------------------------
    //getters and setters
    //------------------------------------------------------------------

    public static void setFullscreen(boolean f) {
        fullscreen = f;
    }

    public int[][] getFrame(){
        return frame;
    }
}