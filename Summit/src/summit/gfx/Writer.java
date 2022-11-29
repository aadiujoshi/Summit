package summit.gfx;

public class Writer extends Thread{

    private volatile Object process = false;

    private int[] finalFrame;
    private int[][] frame;
    private final int finalHeight;
    private final int finalWidth;

    private final int start;
    private final int end;

    public Writer(int start, int end, final int finalWidth, final int finalHeight){
        this.finalWidth = finalWidth;
        this.finalHeight = finalHeight;
        this.start = start;
        this.end = end;
    }

    @Override
    public void run(){
        while(true){
            if((boolean)process){
                float scaleX = finalWidth/Renderer.WIDTH;
                float scaleY = finalHeight/Renderer.HEIGHT;

                for(int r = start; r < end; r++) {
                    for(int c = 0; c < finalWidth; c++){
                        if(Math.round(r/scaleY) < Renderer.HEIGHT && Math.round(c/scaleX) < Renderer.WIDTH){
                            finalFrame[r*finalWidth+c] = frame[Math.round(r/scaleY)][Math.round(c/scaleX)];
                        }
                    }
                }

                endProcess();
            }
        }
    }

    public boolean finished(){
        return !((boolean)process);
    }

    public void startProcess(int[] finalFrame, int[][] frame){
        this.finalFrame = finalFrame;
        this.frame = frame;
        synchronized(process){
            process = true;
        }
    }

    public void endProcess(){
        synchronized(process){
            process = false;
        }
    }
}
