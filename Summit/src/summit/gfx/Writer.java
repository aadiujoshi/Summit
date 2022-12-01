/*
* BPA project by Aadi Joshi, Aditya Bhattaharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.gfx;

import summit.util.Time;

public class Writer extends Thread{

    private volatile Object process = false;

    private volatile int[] finalFrame;
    private volatile int[][] frame;
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
            // Time.nanoDelay(10000);
            if(finalFrame != null && frame != null){
                float scaleX = finalWidth/Renderer.WIDTH;
                float scaleY = finalHeight/Renderer.HEIGHT;

                for(int r = start; r < end; r++) {
                    for(int c = 0; c < finalWidth; c++){
                        if(Math.round(r/scaleY) < Renderer.HEIGHT && Math.round(c/scaleX) < Renderer.WIDTH){
                            finalFrame[r*finalWidth+c] = frame[Math.round(r/scaleY)][Math.round(c/scaleX)];
                        }
                    }
                }

                // endProcess();
            }
        }
    }

    @Deprecated
    public boolean finished(){
        return !((boolean)process);
    }

    public void startProcess(int[] finalFrame, int[][] frame){
        this.finalFrame = finalFrame;
        this.frame = frame;
        process = true;
    }

    @Deprecated
    public void endProcess(){
        process = false;
    }
}
