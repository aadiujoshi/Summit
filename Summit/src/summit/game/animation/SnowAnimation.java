package summit.game.animation;

import summit.gfx.PaintEvent;
import summit.gfx.Paintable;
import summit.gfx.Renderer;
import summit.util.Time;

public class SnowAnimation implements Paintable{

    private int dx; 
    private int dy;

    //updates snow every .25 seconds
    private ScheduledEvent snowfall;

    //front 16 bits are x, and last 16 bits are y
    private long[] particles;

    /**
     * Snow velocities are left positive
     * @param dx
     * @param dy
     */
    public SnowAnimation(int dx, int dy){
        this.dx = dx;
        this.dy = dy; 

        particles = new long[200];

        for (int i = 0; i < particles.length; i++) {
            particles[i] = (((int)(Math.random()*Renderer.WIDTH)) << 16) | 
                            ((int)(Math.random()*Renderer.HEIGHT) << 0);
            // System.out.println(particles[i]);
        }

        snowfall = new ScheduledEvent(Time.MS_IN_S/15, ScheduledEvent.FOREVER) {
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
    public void paint(PaintEvent e) {
        // for(int i = 0; i < 100; i++){
        //     e.getRenderer().fillRect((int)(Math.random()*Renderer.WIDTH), 
        //                                 (int)(Math.random()*Renderer.HEIGHT), 
        //                                 2, 2, 
        //                                 Renderer.toIntRGB(100, 0, 0));
        // }

        for (int i = 0; i < particles.length; i++) {
            e.getRenderer().fillRect((int)((particles[i] >> 16) & 0xff), 
                                        (int)((particles[i] >> 0) & 0xff), 
                                        2, 2, Renderer.toIntRGB(230, 230, 230));
        }
    }
}
