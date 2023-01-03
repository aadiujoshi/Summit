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

/**
 * A class that handles the rendering of graphics in the game.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R. S.
 */
public class Renderer implements Serializable {

    /** 2D array representing the current frame to be rendered. */
    private int[][] frame;

    /** Flag indicating whether the game is currently in fullscreen mode. */
    private static volatile boolean fullscreen;

    // -- used by final frame writer threads --

    /** Array of threads used for concurrent rendering. */
    private final Writer[] writers;

    /**
     * Array representing the final image to be displayed after rendering is
     * completed.
     */
    private int[] finalFrame;

    /** The final height of the rendered image. */
    private final int finalHeight;

    /** The final width of the rendered image. */
    private final int finalWidth;

    /** Field used to sync frame rendering with the display refresh rate. */
    private int vsync = 0;
    // ------------------------------------------
    /** Width of the frame in pixels. */
    public static final int WIDTH = 256;

    /** Height of the frame in pixels. */
    public static final int HEIGHT = 144;

    /** Constant representing no operation. */
    public static final int NO_OP = 0b0;

    /** Constant representing flipping the image along the x-axis. */
    public static final int FLIP_X = 0b1;

    /** Constant representing flipping the image along the y-axis. */
    public static final int FLIP_Y = 0b10;

    /** Constant representing rotating the image by 90 degrees clockwise. */
    public static final int ROTATE_90 = 0b100;

    /** Constant representing a red outline. */
    public static final int OUTLINE_RED = 0b1000;

    /** Constant representing a green outline. */
    public static final int OUTLINE_GREEN = 0b10000;

    /** Constant representing a blue outline. */
    public static final int OUTLINE_BLUE = 0b100000;

    // public static final int IS_PLAYER = 0b1000000;

    /**
     * Constructor for a Renderer object.
     * 
     * @param t       number of concurrent threads to use for rendering
     * @param fWidth  final width of the rendered image
     * @param fHeight final height of the rendered image
     */
    public Renderer(int t, int fWidth, int fHeight) {
        frame = new int[HEIGHT][WIDTH];
        this.finalWidth = fWidth;
        this.finalHeight = fHeight;

        // --------------------------------------------------------------------------
        // concurrent rendering
        // --------------------------------------------------------------------------

        if (t == 1) {
            this.writers = null;
            return;
        }

        this.writers = new Writer[Renderer.getClosestFactor(t, fHeight)];

        for (int i = 0; i < writers.length; i++) {
            final int start = i * (fHeight / t);
            final int end = (i + 1) * (fHeight / t);

            writers[i] = new Writer(start, end, fWidth, fHeight);
        }

        for (Thread wr : writers) {
            wr.setPriority(Thread.NORM_PRIORITY);
            wr.start();
        }
        // ---------------------------------------------------------------------------------------
    }

    /**
     * Terminates the rendering threads.
     */
    public void terminate() {
        if (writers == null)
            return;

        for (int i = 0; i < writers.length; i++) {
            writers[i].terminate();
        }
    }

    /**
     * Resets the frame to be rendered.
     */
    public void resetFrame() {
        frame = new int[HEIGHT][WIDTH];
    }

    // parallelize this process for more frames 🤑🤑🤑🤑

    /**
     * Scales the current frame up to the specified image.
     * 
     * @param newFrame the image to which the frame should be scaled
     */
    public void upscaleToImage(BufferedImage newFrame) {

        finalFrame = ((DataBufferInt) newFrame.getRaster().getDataBuffer()).getData();

        // if set to write with just one thread
        if (writers == null) {
            float scaleX = finalWidth / WIDTH;
            float scaleY = finalHeight / HEIGHT;

            for (int r = 0; r < finalHeight; r++) {
                for (int c = 0; c < finalWidth; c++) {
                    if (Math.round(r / scaleY) < frame.length && Math.round(c / scaleX) < frame[0].length) {
                        finalFrame[r * finalWidth + c] = frame[Math.round(r / scaleY)][Math.round(c / scaleX)];
                    }
                }
            }
        }

        if (writers != null) {
            // signal threads to start upscaling and writing to final frame array
            for (int i = 0; i < writers.length; i++) {
                writers[i].startProcess(finalFrame, frame);
            }

            Time.nanoDelay(Time.NS_IN_MS * (int) Settings.getSetting("vsync"));
        }
    }

    /**
     * does not work
     * 
     * @param scale
     */
    @Deprecated
    public void contrastFrame(float scale) {
        int min = rgbIntensity(frame[0][0]);
        int max = min;
        int avg = (max + min) / 2;

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

                if (ints < avg) {
                    int negint = (int) ((ints - min) * scale);
                    frame[r][c] = toIntRGB(rr - negint, gg - negint, bb - negint);
                } else {
                    int posint = (int) ((max - ints) * scale);
                    frame[r][c] = toIntRGB(r + posint, gg + posint, bb + posint);
                }
            }
        }
    }

    /**
     * 
     * Renders a light source to the frame buffer.
     * 
     * @param light the light source to render
     * 
     * @param cam   the camera to use for rendering
     */
    public void renderLight(Light light, Camera cam) {
        if (light == null || cam == null)
            return;

        Point center = toPixel(light.getX(), light.getY(), cam);

        Light.Shape shape = light.getShape();

        float radius = light.getRadius() * 16;

        // for square lights
        // float sqrVertexDist = light.getRadius()*16*1.41421356f;

        int r = light.getRed();
        int b = light.getBlue();
        int g = light.getGreen();

        for (int xx = (int) (center.x - radius); xx < (int) (center.x + radius); xx++) {
            for (int yy = (int) (center.y - radius); yy < (int) (center.y + radius); yy++) {
                if (!inArrBounds(yy, xx, frame.length, frame[0].length))
                    continue;
                float d = Region.distance(center.x, center.y, xx, yy);

                if (shape == Light.Shape.CIRCLE && d <= radius) {
                    ColorFilter filt = new ColorFilter((int) (r - ((d / radius) * r)), (int) (g - ((d / radius) * g)),
                            (int) (b - ((d / radius) * b)));
                    frame[yy][xx] = filt.filterColor(frame[yy][xx]);
                }
            }
        }
    }

    /**
     * 
     * Filters the pixels within the given rectangular region with the given color
     * filter.
     * 
     * @param x      x-coordinate of the top-left corner of the region
     * 
     * @param y      y-coordinate of the top-left corner of the region
     * 
     * @param width  width of the region
     * 
     * @param height height of the region
     * 
     * @param filter the color filter to use
     */
    public void filterRect(int x, int y, int width, int height, ColorFilter filter) {
        if (filter == null)
            return;

        for (int r = y; r < frame.length && r < y + height; r++) {
            for (int c = x; c < frame[0].length && c < x + width; c++) {
                if (inArrBounds(r, c, frame.length, frame[0].length))
                    frame[r][c] = filter.filterColor(frame[r][c]);
            }
        }
    }

    /**
     * 
     * Renders the given sprite to the frame buffer.
     * 
     * @param sprite    the sprite to render
     * @param x         x-coordinate at which to render the sprite
     * @param y         y-coordinate at which to render the sprite
     * @param operation optional operation to modify the rendering of the sprite
     *                  (e.g. flip, rotate)
     * @param filter    optional color filter to apply to the sprite during
     *                  rendering
     */
    public void render(String sprite, int x, int y, int operation, ColorFilter filter) {
        this.render(BufferedSprites.getSprite(sprite), x, y, operation, filter);
    }

    /**
     * USE THIS METHOD FOR GENERAL RENDERING (MENUS, DIALOGUE, ETC). COORDINATES ARE
     * SCREEN COORDINATES
     */

    /**
     * 
     * Renders the given 2D array of pixels to the frame buffer.
     * 
     * @param sprite    2D array of pixels representing the sprite to render
     * 
     * @param x         x-coordinate at which to render the sprite
     * 
     * @param y         y-coordinate at which to render the sprite
     * 
     * @param operation optional operation to modify the rendering of the sprite
     *                  (e.g. flip, rotate)
     * 
     * @param filter    optional color filter to apply to the sprite during
     *                  rendering
     */
    public void render(int[][] sprite, int x, int y, int operation, ColorFilter filter) {

        if (sprite == null)
            return;

        int nx = Math.round(x - (sprite[0].length / 2)) + 1;
        int ny = Math.round(y - (sprite.length / 2));

        if (operation != NO_OP) {
            if ((operation & ROTATE_90) == ROTATE_90) {
                sprite = rotate(sprite);
            }
            if ((operation & FLIP_X) == FLIP_X) {
                sprite = flip(sprite, FLIP_X);
            }
            if ((operation & FLIP_Y) == FLIP_Y) {
                sprite = flip(sprite, FLIP_Y);
            }
        }

        if ((operation >> 3) != 0) {
            sprite = outline(sprite, operation >> 3);
        }

        // write final sprite
        for (int yy = ny; yy < ny + sprite.length; yy++) {
            for (int xx = nx; xx < nx + sprite[0].length; xx++) {
                if (inArrBounds(yy - ny, xx - nx, sprite.length, sprite[0].length)
                        && inArrBounds(yy, xx, frame.length, frame[0].length) && validRGB(sprite[yy - ny][xx - nx])) {
                    if (filter == null)
                        frame[yy][xx] = sprite[yy - ny][xx - nx];
                    else
                        frame[yy][xx] = filter.filterColor(sprite[yy - ny][xx - nx]); // Renderer.filterColor(sprite[yy-ny][xx-nx],
                                                                                      // filter);
                }
            }
        }
    }

    /**
     * 
     * Renders text to the screen at the specified coordinates. The text will be
     * converted to upper case.
     * 
     * @param text      the text to render
     * 
     * @param x         the x coordinate to render the text at (the text will be
     *                  centered at this x coordinate)
     * 
     * @param y         the y coordinate to render the text at
     * 
     * @param operation the operation to perform when rendering the text (e.g.
     *                  blending modes)
     * 
     * @param filter    the color filter to apply when rendering the text
     */
    public void renderText(String text, int x, int y, int operation, ColorFilter filter) {

        text = text.toUpperCase();

        int offsetX = x - (text.length() * 8 / 2) + 4;

        for (int i = 0; i < text.length(); i++) {
            if (!(text.charAt(i) + "").equals(" "))
                render(text.charAt(i) + Sprite.TEXT_APPEND_KEY, offsetX + (i * 8), y, operation, filter);
        }
    }

    /**
     * USE THIS METHOD FOR RENDERING GAME STUFF (ANYTHING THAT IS POSITIONALLY BASED
     * ON A CAMERA).
     * COORDINATES ARE GAMESPACE COORDINATES.
     */

    /**
     * 
     * Renders a sprite on the screen using gamespace coordinates. The sprite will
     * be adjusted for the camera's position.
     * 
     * Use this method for rendering anything that is positionally based on a
     * camera.
     * 
     * @param sprite    the sprite to render
     * 
     * @param x         the x coordinate of the sprite in gamespace
     * 
     * @param y         the y coordinate of the sprite in gamespace
     * 
     * @param operation the operation to perform when rendering the sprite (e.g.
     *                  blending modes)
     * 
     * @param filter    the color filter to apply when rendering the sprite
     * 
     * @param camera    the camera to use for adjusting the sprite's position on the
     *                  screen
     */
    public void renderGame(String sprite, float x, float y, int operation, ColorFilter filter, Camera camera) {

        // if((operation & IS_PLAYER) == IS_PLAYER){
        // Point spritePos = toPixel(camera.getX(), camera.getY(), camera);
        // operation &= ~IS_PLAYER;
        // this.render(sprite, spritePos.x, spritePos.y, operation, filter);
        // return;
        // }

        // operation &= ~IS_PLAYER;

        Point spritePos = toPixel(x, y, camera);

        this.render(sprite, spritePos.x, spritePos.y, operation, filter);
    }

    /**
     * 
     * Renders an image on the screen with the specified center coordinates.
     * 
     * @param img the image to render
     * @param x   the x coordinate of the center of the image
     * @param y   the y coordinate of the center of the image
     */
    public void renderImage(BufferedImage img, int x, int y) {
        for (int xx = x - (img.getWidth() / 2); xx < x + (img.getWidth() / 2); xx++) {
            for (int yy = y - (img.getHeight() / 2); yy < y + (img.getHeight() / 2); yy++) {
                int rgb = img.getRGB(xx - (x - (img.getWidth() / 2)), yy - (y - (img.getHeight() / 2)));
                if (inArrBounds(yy, xx, frame.length, frame[0].length) && validRGB(rgb))
                    frame[yy][xx] = rgb;
            }
        }
    }

    /**
     * 
     * Fills a rectangle with the specified color. The top left corner of the
     * rectangle is at the specified coordinates.
     * 
     * @param x      the x coordinate of the top left corner of the rectangle
     * @param y      the y coordinate of the top left corner of the rectangle
     * @param width  the width of the rectangle
     * @param height the height of the rectangle
     * @param color  the color to fill the rectangle with
     */
    public void fillRect(int x, int y, int width, int height, int color) {
        for (int x1 = x; x1 < x + width; x1++) {
            for (int y1 = y; y1 < y + height; y1++) {
                if (inArrBounds(y1, x1, frame.length, frame[0].length))
                    frame[y1][x1] = color;
            }
        }
    }

    /**
     * 
     * Returns the frame as a one-dimensional array.
     * 
     * @return the frame as a one-dimensional array
     */
    public int[] frameAsArray() {
        int[] reformatted = new int[frame.length * frame[0].length];

        // holy shit that works #program_in_c
        for (int row = 0; row < frame.length; row++) {
            System.arraycopy(frame[row], 0, reformatted, row * frame[0].length, frame[0].length);
        }

        return reformatted;
    }

    /**
     * 
     * Draws a line on the screen with the specified start and end coordinates and
     * color.
     * 
     * The line can be dotted if specified.
     * 
     * @param sx     the x coordinate of the start of the line
     * 
     * @param sy     the y coordinate of the start of the line
     * 
     * @param ex     the x coordinate of the end of the line
     * 
     * @param ey     the y coordinate of the end of the line
     * 
     * @param color  the color of the line
     * 
     * @param dotted whether or not to draw the line as a dotted line
     */
    public void drawLine(int sx, int sy, int ex, int ey, int color, boolean dotted) {
        if (sx < 0 || ex < 0 || sy < 0 || ey < 0)
            return;
        if (sx > WIDTH || ex > WIDTH || sy > HEIGHT || ey > HEIGHT)
            return;

        if (sx == sy) {
            int start = Math.min(sy, ey);
            int end = Math.max(sy, ey);

            for (int y = start; y <= end; y++) {
                if (dotted && y % 5 != 0)
                    frame[y][sx] = color;
                else if (!dotted)
                    frame[y][sx] = color;
            }

            return;
        }

        float m = (float) (ey - sy) / (float) (ex - sx);
        int b = (int) (-1 * (m * sx - sy));

        int start = Math.min(sx, ex);
        int end = Math.max(sx, ex);

        for (float inc_x = start; inc_x < end; inc_x += (1f / WIDTH)) {
            if (dotted && Math.round(inc_x) % 5 != 0)
                frame[(int) (inc_x * m + b)][Math.round(inc_x)] = color;
            else if (!dotted)
                frame[(int) (inc_x * m + b)][Math.round(inc_x)] = color;
        }
    }

    // --------------------------------------------------------------------
    // utility methods
    // --------------------------------------------------------------------

    // just for thread

    /**
     * 
     * Returns the closest factor of the target to the given number. If the target
     * cannot be reached exactly, the closest possible factor will be returned.
     * 
     * @param target the target factor to find
     * @param number the number to find the closest factor for
     * @return the closest factor of the target to the given number
     */
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

    /**
     * 
     * Converts the given red, green, and blue values to an integer representation
     * of a color. If any of the values exceed 255, they will be set to 255.
     * 
     * @param r the red value of the color (0-255)
     * @param g the green value of the color (0-255)
     * @param b the blue value of the color (0-255)
     * @return an integer representation of the color
     */
    public static int toIntRGB(int r, int g, int b) {
        return ((r > 255) ? 255 : r << 16) |
                ((g > 255) ? 255 : g << 8) |
                ((b > 255) ? 255 : b);
    }

    /**
     * 
     * Returns whether or not the given integer represents a valid RGB color.
     * 
     * @param rgb the integer to check
     * @return true if the integer represents a valid RGB color, false otherwise
     */
    public static boolean validRGB(int rgb) {
        return rgb != -1;
    }

    /**
     * 
     * Calculates the intensity of the given color by averaging its red, green, and
     * blue values.
     * 
     * @param color the color to calculate the intensity of
     * @return the intensity of the color
     */
    public static int rgbIntensity(int color) {
        return (((color >> 16) & 0xff) +
                ((color >> 8) & 0xff) +
                ((color >> 0) & 0xff)) / 3;
    }

    /**
     * Camera is left in gamecoordinates
     */

    /**
     * 
     * Converts the given game space coordinates to pixel coordinates using the
     * given camera. The camera is left in game space coordinates.
     * 
     * @param x   the x coordinate in game space
     * 
     * @param y   the y coordinate in game space
     * 
     * @param cam the camera to use for the conversion
     * 
     * @return the pixel coordinates
     */
    public static java.awt.Point toPixel(float x, float y, Camera cam) {
        float nx = (WIDTH / 2) + (x * 16F) - (cam.getX() * 16F);
        float ny = (HEIGHT / 2) - (y * 16F) + (cam.getY() * 16F);

        return new java.awt.Point(Math.round(nx), Math.round(ny));
    }

    /**
     * 
     * Converts the given mouse coordinates to tile coordinates in game space using
     * the given camera. The mouse coordinates are assumed to be in renderer pixel
     * space (256x144).
     * 
     * @param mx  the mouse x coordinate in renderer pixel space
     * 
     * @param my  the mouse y coordinate in renderer pixel space
     * 
     * @param cam the camera to use for the conversion
     * 
     * @return the tile coordinates in game space
     */
    public static Point2D.Float toTile(int mx, int my, Camera cam) {
        float rx = (cam.getX() - ((WIDTH / 16f) / 2)) + (mx / 16f);// - 0.1f;
        float ry = (cam.getY() + ((HEIGHT / 16f) / 2)) - (my / 16f) + (fullscreen ? 0 : 0.25f);

        return new Point2D.Float(rx, ry);
    }

    /**
     * 
     * Rotates the given array 90 degrees counterclockwise.
     * 
     * @param arr the array to rotate
     * 
     * @return the rotated array
     */
    public static int[][] rotate(int[][] arr) {
        int[][] rotated = new int[arr.length][arr[0].length];

        for (int r = 0; r < arr.length; r++) {
            for (int c = 0; c < arr[0].length; c++) {
                // transform matrix
                // prev column index becomes row index
                // arr[0].length-r-1 for new column index

                rotated[c][arr[0].length - r - 1] = arr[r][c];
            }
        }

        return arr = rotated;
    }

    /**
     * 
     * Flips the given array horizontally, vertically, or both according to the
     * transform flag.
     * 
     * @param arr       the array to flip
     * 
     * @param transform the transform flag, which can be FLIP_X, FLIP_Y, or FLIP_X |
     *                  FLIP_Y
     * 
     * @return the flipped array
     */
    public static int[][] flip(int[][] arr, int transform) {
        int[][] transformed = new int[arr.length][arr[0].length];

        if ((transform & FLIP_X) == FLIP_X) {
            for (int r = 0; r < arr.length; r++) {
                for (int c = 0; c < arr[0].length / 2 + 1; c++) {
                    transformed[r][c] = arr[r][arr[0].length - c - 1];
                    transformed[r][arr[0].length - c - 1] = arr[r][c];
                }
            }
        }
        if ((transform & FLIP_Y) == FLIP_Y) {
            for (int r = 0; r < arr.length / 2 + 1; r++) {
                for (int c = 0; c < arr[0].length; c++) {
                    transformed[r][c] = arr[arr.length - r - 1][c];
                    transformed[arr.length - r - 1][c] = arr[r][c];
                }
            }
        }

        return arr = transformed;
    }

    /**
     * 
     * Outlines the given array of pixel values with the given color, leaving the
     * original flag packed.
     * 
     * @param s     the array of pixel values to outline
     * 
     * @param color the color to use for the outline
     * 
     * @return the outlined array of pixel values
     */
    public static int[][] outline(int[][] sprite, int flag) {

        // unpack flag
        flag <<= 3;
        int color = ((flag & OUTLINE_RED) == OUTLINE_RED ? 0xff << 16 : 0)
                | ((flag & OUTLINE_GREEN) == OUTLINE_GREEN ? 0xff << 8 : 0)
                | ((flag & OUTLINE_BLUE) == OUTLINE_BLUE ? 0xff << 0 : 0);

        int[][] outlined = new int[sprite.length][sprite[0].length];

        for (int r = 0; r < sprite.length; r++) {
            for (int c = 0; c < sprite[0].length; c++) {
                if (sprite[r][c] != -1) {
                    if (
                    // check if on edge of sprite and is valid rgb
                    (r == 0) ||
                            (r == sprite.length - 1) ||
                            (c == 0) ||
                            (c == sprite[0].length - 1) ||

                            // check sides
                            (r - 1 > -1 && sprite[r - 1][c] == -1) ||
                            (c - 1 > -1 && sprite[r][c - 1] == -1) ||
                            (r + 1 < sprite.length && sprite[r + 1][c] == -1) ||
                            (c + 1 < sprite[0].length && sprite[r][c + 1] == -1) ||

                            // check corners
                            (r - 1 > -1 && c - 1 > -1 && sprite[r - 1][c - 1] == -1) ||
                            (r - 1 > -1 && c + 1 < sprite[0].length && sprite[r - 1][c + 1] == -1) ||
                            (r + 1 < sprite.length && c - 1 > -1 && sprite[r + 1][c - 1] == -1) ||
                            (r + 1 < sprite.length && c + 1 < sprite[0].length && sprite[r + 1][c + 1] == -1)) {

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

    /**
     * 
     * Returns whether or not the given row and column indices are within the bounds
     * of the given array.
     * 
     * @param row     the row index to check
     * @param col     the column index to check
     * @param numRows the number of rows in the array
     * @param numCols the number of columns in the array
     * @return true if the indices are within the bounds of the array, false
     *         otherwise
     */
    public static boolean inArrBounds(int r, int c, int rows, int cols) {
        return (r >= 0) && (r < rows) && (c >= 0) && (c < cols);
    }

    // ------------------------------------------------------------------
    // getters and setters
    // ------------------------------------------------------------------

    /**
     * 
     * Sets the fullscreen mode for the renderer.
     * 
     * @param f true for fullscreen mode, false for windowed mode
     */
    public static void setFullscreen(boolean f) {
        fullscreen = f;
    }

    
    /**
     * 
     * Returns the current frame being rendered by the renderer.
     * 
     * @return the current frame
     */
    public int[][] getFrame() {
        return frame;
    }
}