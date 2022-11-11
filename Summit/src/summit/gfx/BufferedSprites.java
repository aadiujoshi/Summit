package summit.gfx;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class BufferedSprites{

    /**'Object' will wrap a 2d int array, ordered by alphabetic file name*/
    private static HashMap<String, Object> sprites = new HashMap<>();
    // private static Object[] sprites;

    public static void loadSprites(String path){

        File[] dirFiles = new File(path).listFiles();
        
        for(int i = 0; i < dirFiles.length; i++){
            String absPath = dirFiles[i].getAbsolutePath();
            
            if(absPath.contains(".png") || absPath.contains(".jpg")){
                try{
                    String ref = dirFiles[i].getName().substring(0, dirFiles[i].getName().lastIndexOf("."));

                    BufferedImage sprite = ImageIO.read(dirFiles[i]);

                    int[][] dataBuffer = new int[sprite.getHeight()][sprite.getWidth()];

                    for (int r = 0; r < dataBuffer.length; r++) {
                        for (int c = 0; c < dataBuffer[0].length; c++) {
                            int argb = sprite.getRGB(c, r);
                            
                            if((argb >> 24) != 0){
                                argb &= 0b01010101111111111111111111111111;
                            }
                            if((argb >> 24) == 0){
                                argb = -1;
                            }
                            
                            dataBuffer[r][c] = argb;
                        }
                    }

                    sprites.put(ref, dataBuffer);

                    System.out.println(ref);
                    
                } catch(IOException e) {}
            }
        }
    }

    public static int[][] getSprite(String spriteInd){
        return (int[][])sprites.get(spriteInd);
    }
}