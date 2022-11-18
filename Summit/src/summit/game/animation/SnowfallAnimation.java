package summit.game.animation;

import java.util.ArrayList;

import summit.gfx.OrderPaintEvent;
import summit.gfx.PaintEvent;
import summit.gfx.Paintable;
import summit.gfx.RenderLayers;
import summit.gfx.Renderer;
import summit.util.Time;

public class SnowfallAnimation implements Paintable{

    private int dx; 
    private int dy;

    private ScheduledEvent snowfall;

    //front 16 bits are x, and last 16 bits are y
    private long[] particles;

    /**
     * Snow velocities are left positive
     * @param dx
     * @param dy
     */
    public SnowfallAnimation(int dx, int dy){
        this.dx = dx;
        this.dy = dy; 

        particles = new long[200];

        for (int i = 0; i < particles.length; i++) {
            particles[i] = (((int)(Math.random()*Renderer.WIDTH)) << 16) | 
                            ((int)(Math.random()*Renderer.HEIGHT) << 0);
        }

        snowfall = new ScheduledEvent(Time.MS_IN_S/100, ScheduledEvent.FOREVER) {
            @Override
            public void run() {
                for (int i = 0; i < particles.length; i++) {

                    //random offset
                    int cx = Math.random() > 0.5 ? 1 : -1;
                    int cy = Math.random() > 0.5 ? 1 : -1;

                    int nx = (int)((particles[i] >> 16) & 0xff) + dx + cx;
                    int ny = (int)((particles[i] >> 0) & 0xff) + dy + cy;

                    particles[i] = (nx << 16) | (ny << 0);
                }
            }
        };

        Scheduler.registerEvent(snowfall);
    }


    @Override
    public void renderLayer(OrderPaintEvent ope) {
        ope.getRenderLayers().addToLayer(RenderLayers.STRUCTURE_ENTITY_LAYER+2, this);
    }

    @Override
    public void paint(PaintEvent e) {
        for (int i = 0; i < particles.length; i++) {
            e.getRenderer().fillRect((int)((particles[i] >> 16) & 0xff), 
                                        (int)((particles[i] >> 0) & 0xff), 
                                        1, 1, Renderer.toIntRGB(230, 230, 230));
        }
    }
}