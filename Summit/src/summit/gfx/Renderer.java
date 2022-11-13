package summit.gfx;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Renderer {

    private int[][] frame;
    
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
    

    public Renderer(){
        frame = new int[HEIGHT][WIDTH];
    }

    public void resetFrame(){
        frame = new int[HEIGHT][WIDTH]; 
    }

    public void upscaleToImage(BufferedImage newFrame){
        int newWidth = newFrame.getWidth();
        int newHeight = newFrame.getHeight();

        // BufferedImage newFrame = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);

        int[] frameBuffer = ((DataBufferInt)newFrame.getRaster().getDataBuffer()).getData();

        float scaleX = newWidth/WIDTH;
        float scaleY = newHeight/HEIGHT;
        
        // method 1 (working)
        for(int r = 0; r < newHeight; r++) {
            for(int c = 0; c < newWidth; c++){
                if(Math.round(r/scaleY) < frame.length && Math.round(c/scaleX) < frame[0].length){
                    frameBuffer[r*newWidth+c] = frame[Math.round(r/scaleY)][Math.round(c/scaleX)];
                }
            }
        }
    }


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
    public void renderLight(Light light, float x, float y, Camera cam){
        Point2D.Float center = toPixel(x, y, cam);

        float radius = light.getRadius()*16;
        int intensity = light.getIntensity();

        for(int xx = (int)(center.x-radius); xx < (int)(center.x+radius); xx++){
            for (int yy = (int)(center.y-radius); yy < (int)(center.y+radius); yy++) {
                if(!inArrBounds(yy, xx, frame.length, frame[0].length))
                    continue;
                float d = distance(center.x, center.y, xx, yy);
                if(d <= radius){
                    frame[yy][xx] = setIntensity(frame[yy][xx], (int)(intensity-((d/radius)*intensity)));
                }
            }
        }
    }

    /**
     * val is brightness (make negative to dim frame)
     * @param val
     */
    public void frameBrightness(int val){
        for (int r = 0; r < frame.length; r++) {
            for (int c = 0; c < frame[0].length; c++) {
                frame[r][c] = setIntensity(frame[r][c], val);
            }
        }
    }

    /**
     * USE THIS METHOD FOR GENERAL RENDERING (MENUS, DIALOGUE, ETC). COORDINATES ARE SCREEN COORDINATES
     */
    public void render(String s, float x, float y, int operation){
        
        int[][] sprite = BufferedSprites.getSprite(s);

        int nx = Math.round(x-(sprite[0].length/2));
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
                if(inArrBounds(yy-ny, xx-nx, sprite.length, sprite[0].length) && 
                    inArrBounds(yy, xx, frame.length, frame[0].length) && validRGB(sprite[yy-ny][xx-nx]))
                    frame[yy][xx] = sprite[yy-ny][xx-nx];
            }
        }
    }

    public void renderText(String text, int x, int y, int operation){

        int offsetX = x-(text.length()*8/2) + 4;

        for(int i = 0; i < text.length(); i++) {
            if(!(text.charAt(i)+"").equals(" "))
                render(text.charAt(i)+Sprite.TEXT_APPEND_KEY, offsetX+(i*8), y, operation);
        }
    }

    /** 
     * USE THIS METHOD FOR RENDERING GAME STUFF (ANYTHING THAT IS POSITIONALLY BASED ON A CAMERA).
     * COORDINATES ARE GAMESPACE COORDINATES.
    */
    public void renderGame(String s, float x, float y, int operation, Camera camera){

        Point2D.Float spritePos = toPixel(x, y, camera);
        
        this.render(s, spritePos.x, spritePos.y, operation);
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
        int[] reformated = new int[frame.length*frame[0].length];

        //holy shit that works #program_in_c
        for(int row = 0; row < frame.length; row++) {
            System.arraycopy( frame[row],0 ,reformated ,row*frame[0].length ,frame[0].length);
        }

        return reformated;
    }

    //--------------------------------------------------------------------
    // utility methods
    //--------------------------------------------------------------------

    public static float distance(float x1, float y1, float x2, float y2){
        return (float)Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
    }

    public static int toIntRGB(int r, int g, int b){
        return ((r > 255) ? 255 : r << 16) | 
                ((g > 255) ? 255 : g << 8) | 
                ((b > 255) ? 255 : b);
    }

    public static boolean validRGB(int rgb){
        return rgb != -1;
    }

    public static int setIntensity(int color, int intensity){
        
        int red = ((color >> 16) & 0xff);
        int green = ((color >> 8) & 0xff);
        int blue = ((color >> 0) & 0xff);
        
        red = ((red+intensity > 255) ? 255: red+intensity);
        green = ((green+intensity > 255) ? 255: green+intensity);
        blue = ((blue+intensity > 255) ? 255: blue+intensity);
        
        red = ((red < 0) ? 0: red);
        green = ((green < 0) ? 0: green);
        blue = ((blue < 0) ? 0: blue);

        return (red << 16) | (green << 8) | (blue << 0);
    }

    public static int rgbIntensity(int color){
        return (((color >> 16) & 0xff) + 
                ((color >> 8) & 0xff) + 
                ((color >> 0) & 0xff))/3;
    }

    /**
     * Camera is left in gamecoordinates
     */
    public static Point2D.Float toPixel(float x, float y, Camera cam){
        float nx = (WIDTH/2)+(x*16F)-(cam.getX()*16F);
        float ny = (HEIGHT/2)-(y*16F)+(cam.getY()*16F);

        return new Point2D.Float(nx, ny);
    }

    /** 
     * Rotate 90 degrees clockwise
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
                    if( //check sides
                        (r-1 > -1 && sprite[r-1][c] == -1) ||
                        (c-1 > -1 && sprite[r][c-1] == -1) ||
                        (r+1 < sprite.length && sprite[r+1][c] == -1) ||
                        (c+1 < sprite[0].length && sprite[r][c+1] == -1) || 
                        
                        //check corners
                        (r-1 > -1 && c-1 > -1 && sprite[r-1][c-1] == -1) || 
                        (r-1 > -1 && c+1 < sprite[0].length && sprite[r-1][c+1] == -1) ||
                        (r+1 < sprite.length && c-1 > -1 && sprite[r+1][c-1] == -1) || 
                        (r+1 < sprite.length && c+1 < sprite[0].length) && sprite[r+1][c+1] == -1){
                        
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

    public int[][] getFrame(){
        return frame;
    }
}

