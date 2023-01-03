
package summit.gfx;

/**
 * 
 * The {@link Writer} class is responsible for taking the final frame data from
 * the {@link Renderer} class, and scaling it to fit the window size specified
 * in
 * the {@link GameWindow} class. It is implemented as a Thread to prevent the
 * rendering process from being
 * blocked.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R. S.
 */
public class Writer extends Thread {

    /** Flag to control the execution of the Thread */
    private volatile boolean process = true;

    /** The final frame data to be rendered */
    private volatile int[] finalFrame;

    private volatile int[][] frame;
    private final int finalHeight;
    private final int finalWidth;

    private final int start;
    private final int end;

    /**
     * Constructs a new Writer thread with the given start and end index for the
     * rows of the finalFrame
     * that it should write to, and the final width and height of the finalFrame.
     * 
     * @param start       The start index for the rows in the finalFrame that the
     *                    thread should write to
     * @param end         The end index for the rows in the finalFrame that the
     *                    thread should write to
     * @param finalWidth  The width of the finalFrame
     * @param finalHeight The height of the finalFrame
     */
    public Writer(int start, int end, final int finalWidth, final int finalHeight) {
        this.finalWidth = finalWidth;
        this.finalHeight = finalHeight;
        this.start = start;
        this.end = end;
    }

    @Override
    public void run() {
        while (process) {
            if (finalFrame != null && frame != null) {
                float scaleX = finalWidth / Renderer.WIDTH;
                float scaleY = finalHeight / Renderer.HEIGHT;

                for (int r = start; r < end; r++) {
                    for (int c = 0; c < finalWidth; c++) {
                        if (Math.round(r / scaleY) < Renderer.HEIGHT && Math.round(c / scaleX) < Renderer.WIDTH) {
                            finalFrame[r * finalWidth + c] = frame[Math.round(r / scaleY)][Math.round(c / scaleX)];
                        }
                    }
                }
            }
        }

        System.out.println("Writer Thread Terminated");
    }

    /**
     * Sets the finalFrame and frame to be written to it for this Writer thread.
     * 
     * @param finalFrame The finalFrame to be written to
     * @param frame      The frame to be written to the finalFrame
     */
    public void startProcess(int[] finalFrame, int[][] frame) {
        this.finalFrame = finalFrame;
        this.frame = frame;
    }

    /**
     * Terminates this Writer thread.
     */
    public void terminate(){
        this.process = false;
    }
}
