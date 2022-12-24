/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.animation;

import summit.gfx.OrderPaintEvent;
import summit.gfx.PaintEvent;
import summit.gfx.RenderLayers;
import summit.gfx.Renderer;
import summit.util.ScheduledEvent;
import summit.util.GameScheduler;
import summit.util.GraphicsScheduler;
import summit.util.Time;

public class ForegroundAnimation extends Animation{

    private int dx; 
    private int dy;

    private int pColor;
    
    //front 16 bits are x, and last 16 bits are y
    private long[] particles;

    /**
     * particle velocities are left positive
     * @param dx
     * @param dy
     */
    public ForegroundAnimation(int dx, int dy, int pColor){
        super(Time.MS_IN_S/100, ScheduledEvent.FOREVER);

        this.dx = dx;
        this.dy = dy; 

        this.pColor = pColor;

        particles = new long[200];

        for (int i = 0; i < particles.length; i++) {
            particles[i] = (((int)(Math.random()*Renderer.WIDTH)) << 16) | 
                            ((int)(Math.random()*Renderer.HEIGHT) << 0);
        }

        GraphicsScheduler.registerEvent(this);
    }

    @Override
    public void run(){
        for (int i = 0; i < particles.length; i++) {

            //random offset
            int cx = Math.random() > 0.5 ? 1 : -1;
            int cy = Math.random() > 0.5 ? 1 : -1;

            int nx = (int)((particles[i] >> 16) & 0xff) + dx + cx;
            int ny = (int)((particles[i] >> 0) & 0xff) + dy + cy;

            particles[i] = (nx << 16) | (ny << 0);
        }
    }

    @Override
    public void setRenderLayer(OrderPaintEvent ope) {
        ope.getRenderLayers().addToLayer(RenderLayers.TOP_LAYER, this);
    }

    @Override
    public void paint(PaintEvent e) {
        for (int i = 0; i < particles.length; i++) {
            e.getRenderer().fillRect((int)((particles[i] >> 16) & 0xff), 
                                        (int)((particles[i] >> 0) & 0xff), 
                                        1, 1, pColor);
        }
    }
}
