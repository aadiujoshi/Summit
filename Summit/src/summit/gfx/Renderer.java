package summit.gfx;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import summit.util.Time;

public class Renderer {
    
    private int[][] frame;

    public static int iterations = 0;

    public static final int WIDTH = 256;
    public static final int HEIGHT = 144;

    public static final int FLIP_NONE = 0b0;
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
        // System.out.println(iterations);
        iterations = 0;
    }

    public void upscaleToImage(BufferedImage newFrame){
        int newWidth = newFrame.getWidth();
        int newHeight = newFrame.getHeight();

        // BufferedImage newFrame = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);

        int[][] upscaled = new int[newHeight][newWidth];

        float scaleX = newWidth/WIDTH;
        float scaleY = newHeight/HEIGHT;

        long strt = Time.timeNs();

        // method 1 (working)
        for(int r = 0; r < upscaled.length; r++) {
            for(int c = 0; c < upscaled[0].length; c++){
                iterations++;
                if(Math.round(r/scaleY) < frame.length && Math.round(c/scaleX) < frame[0].length){
                    upscaled[r][c] = frame[Math.round(r/scaleY)][Math.round(c/scaleX)];
                }
            }
        }
        
        this.frame = upscaled;

        // System.out.println((Time.timeNs()-strt*1f)/Time.NS_IN_MS + "   ");

        newFrame.getRaster().setDataElements(0, 0, newWidth, newHeight, frameAsArray());

        // final byte[] pixels = ((DataBufferByte) newFrame.getRaster().getDataBuffer()).getData();

        //really cool effect i accidentaly created
        // for(int r = 1; r < newHeight; r+=2) {
        //     int r1 = Math.round(r/scaleY);
        //     int r2 = Math.round((r-1)/scaleY);

        //     if(!(r1 < frame.length && r2 < frame.length))
        //         continue;
            
        //     int[] row1 = frame[r1];
        //     int[] row2 = frame[r2];

        //     for(int c = 0; c < newWidth; c++){
        //         iterations++;

        //         int cc = Math.round(c/scaleX);
        //         if(!(cc < frame[0].length))
        //             continue;
                
        //         newFrame.setRGB(c, r, row1[cc]);
        //         newFrame.setRGB(c, r, row2[cc]);
        //     }
        // }
    }


    /**
     * USE THIS METHOD FOR GENERAL RENDERING (MENUS, DIALOGUE, ETC). COORDINATES ARE SCREEN COORDINATES
     */
    public void render(String s, float x, float y, int operation){
        
        int[][] sprite = BufferedSprites.getSprite(s);

        int nx = Math.round(x-(sprite[0].length/2));
        int ny = Math.round(y-(sprite.length/2));

        if(operation != FLIP_NONE){
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
                iterations++;
                if(inArrBounds(yy-ny, xx-nx, sprite.length, sprite[0].length) && 
                    inArrBounds(yy, xx, frame.length, frame[0].length) && validRGB(sprite[yy-ny][xx-nx]))
                    frame[yy][xx] = sprite[yy-ny][xx-nx];
            }
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
                iterations++;
                int rgb = img.getRGB(xx - (x-(img.getWidth()/2)), yy - (y-(img.getHeight()/2)));
                if(inArrBounds(yy, xx, frame.length, frame[0].length) && validRGB(rgb))
                    frame[yy][xx] = rgb;
            }
        }
    }

    public int[] frameAsArray(){
        int[] reformated = new int[frame.length*frame[0].length];

        //holy shit that works thank you jni
        for(int row = 0; row < frame.length; row++) {
            System.arraycopy( frame[row],0 ,reformated ,row*frame[0].length ,frame[0].length);
        }
        
        return reformated;
    }

    //--------------------------------------------------------------------
    // utility methods
    //--------------------------------------------------------------------

    public static boolean validRGB(int rgb){
        return rgb != -1;
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
                iterations++;
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
                    iterations++;
                    transformed[r][c] = arr[r][arr[0].length-c-1];
                    transformed[r][arr[0].length-c-1] = arr[r][c];
                }
            }
        }
        if((transform & FLIP_Y) == FLIP_Y){
            for(int r = 0; r < arr.length/2+1; r++){
                for(int c = 0; c < arr[0].length; c++){
                    iterations++;
                    transformed[r][c] = arr[arr.length-r-1][c];
                    transformed[arr.length-r-1][c] = arr[r][c];
                }
            }
        }

        return arr = transformed;
    }

    /**
     * leave color packed 
     * @param s
     * @param color
     * @return
     */
    public static int[][] outline(int[][] sprite, int flag){

        //unpack flag
        int color =  ((flag & OUTLINE_RED) == OUTLINE_RED ? 0xff << 16 : 0)
                        | ((flag & OUTLINE_GREEN) == OUTLINE_GREEN ? 0xff << 8 : 0)
                        | ((flag & OUTLINE_BLUE) == OUTLINE_BLUE ? 0xff << 0 : 0);

        int[][] outlined = new int[sprite.length][sprite[0].length];

        for(int r = 0; r < sprite.length; r++){
            for(int c = 0; c < sprite[0].length; c++){
                if(sprite[r][c] != -1){
                    if(sprite[r-1][c] == -1 ||
                        sprite[r][c-1] == -1 ||
                        sprite[r+1][c] == -1 ||
                        sprite[r][c+1] == -1){
                        
                        outlined[r][c] = color;
                    }
                } else {
                    outlined[r][c] = sprite[r][c];
                }
            }
        }

        return outlined;
    }

    /**
     * Camera is left in gamecoordinates
     */
    // public static Point2D.Float toTile(float x, float y, Camera cam){
        
    //     //inverted equation of topixel
    //     //REDO IT IS WRONG 
    //     float nx = (x+cam.getX()-(WIDTH/2))/16F;
    //     float ny = (y-cam.getY()-(HEIGHT/2))/(-16F);

    //     return new Point2D.Float(nx, ny); 
    // }

    public static boolean onScreen(float x, float y, Camera cam){
        return false;
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

