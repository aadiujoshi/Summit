
package summit.gfx;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import summit.Main;

/**
 * BufferedSprites is a utility class that loads and stores image sprites in a
 * HashMap
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R. S.
 */
public class BufferedSprites {

    /**
     * A HashMap that stores the sprites, with the keys being the names of the
     * sprites and the values being the
     * pixel data for the sprites.
     * 
     * @see summit.gfx.Sprite
     */
    private static HashMap<String, int[][]> sprites = new HashMap<>();

    /**
     * An array of characters that correspond to the characters in the text sheet.
     */
    public final static char[] charRefs = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            '!', '/', '<', '>', ':', '+', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '-', '=', '(', ')', '.',
            '?' };

    /**
     * Loads all the sprites in the resources/sprites directory and stores them in
     * the sprites HashMap.
     */
    public static void loadSprites() {

        File[] dirFiles = new File(Main.path + "resources/sprites").listFiles();

        for (int i = 0; i < dirFiles.length; i++) {
            String absPath = dirFiles[i].getAbsolutePath();

            if (absPath.contains(".png") || absPath.contains(".jpg")) {
                try {
                    String ref = dirFiles[i].getName().substring(0, dirFiles[i].getName().lastIndexOf("."));

                    BufferedImage sprite = ImageIO.read(dirFiles[i]);

                    int[][] dataBuffer = new int[sprite.getHeight()][sprite.getWidth()];

                    if (ref.equals("textsheet")) {
                        int ind = 0;
                        for (int r = 0; r < dataBuffer.length; r += 8) {
                            for (int c = 0; c < dataBuffer[0].length; c += 8) {

                                int[][] charSprite = new int[8][8];

                                for (int r1 = r; r1 < r + 8; r1++) {
                                    for (int c1 = c; c1 < c + 8; c1++) {
                                        int argb = sprite.getRGB(c1, r1);

                                        if ((argb >> 24) != 0) {
                                            // make text black
                                            argb &= 0b01010101000000000000000000000000;
                                        }
                                        if ((argb >> 24) == 0) {
                                            argb = -1;
                                        }

                                        charSprite[r1 - r][c1 - c] = argb;
                                    }
                                }

                                sprites.put((charRefs[ind] + Sprite.TEXT_APPEND_KEY), charSprite);

                                ind++;

                            }
                        }
                    } else {
                        for (int r = 0; r < dataBuffer.length; r++) {
                            for (int c = 0; c < dataBuffer[0].length; c++) {
                                int argb = sprite.getRGB(c, r);

                                if ((argb >> 24) != 0) {
                                    argb &= 0b01010101111111111111111111111111;
                                }
                                if ((argb >> 24) == 0) {
                                    argb = -1;
                                }

                                dataBuffer[r][c] = argb;
                            }
                        }

                        sprites.put(ref, dataBuffer);

                        System.out.println(ref);
                    }

                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * Returns the sprite with the specified name. If the sprite does not exist,
     * returns a sprite with a single
     * transparent pixel.
     * 
     * @param spriteInd the name of the sprite to retrieve
     * @return the sprite with the specified name
     */

    public static int[][] getSprite(String spriteInd) {
        if (spriteInd == null)
            return new int[][] { { -1 } };

        return (int[][]) sprites.get(spriteInd);
    }
}