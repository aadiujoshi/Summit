package summit.gfx;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class Renderer {
    
    private int[][] frame;

    public static final int WIDTH = 256;
    public static final int HEIGHT = 144;

    public static final int FLIP_NONE = 0b0;
    public static final int FLIP_X = 0b1;
    public static final int FLIP_Y = 0b10;
    public static final int ROTATE_90 = 0b100;

    public Renderer(){
        frame = new int[HEIGHT][WIDTH];
    }

    public void resetFrame(){
        frame = new int[HEIGHT][WIDTH]; 
    }

    public void uspscale(int newWidth, int newHeight){

        int[][] newFrame = new int[newHeight][newWidth];

        float scaleX = newWidth/WIDTH;
        float scaleY = newHeight/HEIGHT;


        //cool looking spaced pixels
        // for (int r = 0; r < frame.length; r++) {
        //     for (int c = 0; c < frame[0].length; c++) {
        //         newFrame[Math.round(r*scaleY)][Math.round(c*scaleX)] = frame[r][c];
        //     }
        // }

        for(int r = 0; r < newFrame.length; r++) {
            for(int c = 0; c < newFrame[0].length; c++){
                if(Math.round(r/scaleY) < frame.length && Math.round(c/scaleX) < frame[0].length)
                    newFrame[r][c] = frame[Math.round(r/scaleY)][Math.round(c/scaleX)];
            }
        }

        this.frame = newFrame;
    }


    /**
     * USE THIS METHOD FOR GENERAL RENDERING (MENUS, DIALOGUE, ETC). COORDINATES ARE SCREEN COORDINATES
     */
    public void render(String s, float x, float y, int transform){
        
        int[][] sprite = BufferedSprites.getSprite(s);

        int nx = Math.round(x-(sprite[0].length/2));
        int ny = Math.round(y-(sprite.length/2));

        if(transform != FLIP_NONE){
            if((transform & ROTATE_90) == ROTATE_90){
                sprite = rotate(sprite);
            }
            if((transform & FLIP_X) == FLIP_X){
                sprite = flip(sprite, FLIP_X);
            }
            if((transform & FLIP_Y) == FLIP_Y){
                sprite = flip(sprite, FLIP_Y);
            }
        }       

        //write final sprite
        for(int yy = ny; yy < ny+sprite.length; yy++) {
            for(int xx = nx; xx < nx+sprite[0].length; xx++) {
                //fully transparent pixel
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
    public void renderGame(String s, float x, float y, int flip, Camera camera){

        Point2D.Float spritePos = toPixel(x, y, camera);
        System.out.println(camera);
        this.render(s, spritePos.x, spritePos.y, flip);
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

    public int[] frameAsArray(){
        int[] reformated = new int[frame.length*frame[0].length];

        for(int i = 0; i < reformated.length; i++) {
            reformated[i] = frame[ i/frame.length ][ i%frame[0].length ]; 
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
