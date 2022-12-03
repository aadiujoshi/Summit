/*
* BPA project by Aadi Joshi, Aditya Bhattaharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.animation;

import java.awt.Point;

import summit.gfx.OrderPaintEvent;
import summit.gfx.PaintEvent;
import summit.gfx.Paintable;
import summit.gfx.RenderLayers;
import summit.gfx.Renderer;
import summit.util.ScheduledEvent;
import summit.util.Scheduler;
import summit.util.Time;

public class ParticleAnimation extends ScheduledEvent implements Paintable{

    private int color;

    //particle velocities in GAME TILES
    private float[] pdx;
    private float[] pdy;

    //particle position
    private float[] px;
    private float[] py;

    private int pCount;

    private static final float gravity = -5;

    private float sx;
    private float sy;

    //x and y are game coords
    public ParticleAnimation(float x, float y, int duration_ms, int particleCount, int color) {
        super(1, duration_ms);

        pdx = new float[particleCount];
        pdy = new float[particleCount];
        px = new float[particleCount];
        py = new float[particleCount];

        this.sx = x;
        this.sy = y;
        this.pCount = particleCount;
        this.color = color;

        for(int i = 0; i < particleCount; i++) {
            pdx[i] = (float)Math.random()*3-1.5f;
            pdy[i] = (float)Math.random()*3;
            px[i] = sx;
            py[i] = sy;
        }

        Scheduler.registerEvent(this);
    }

    @Override
    public void setRenderLayer(OrderPaintEvent ope) {
        ope.addToLayer(RenderLayers.TILE_LAYER+1, this);
    }

    @Override
    public void paint(PaintEvent e) {
        if(terminate()) return;

        for (int i = 0; i < pCount; i++) {
            Point p = Renderer.toPixel(px[i], py[i], e.getCamera());
            e.getRenderer().fillRect(p.x, p.y, 1, 1, color);
        }
    }

    @Override
    public void run() {

        //time elapsed in seconds
        float delta_t = (Time.timeMs()-getInitTime())/1000f;

        for (int i = 0; i < pCount; i++) {

            //delta pos from velocities
            float ndx = pdx[i]*delta_t;
            float ndy = pdy[i]*delta_t;

            float nx = (sx + ndx);
            float ny = (sy + ndy + (gravity/2)*(delta_t*delta_t));

            px[i] = nx;
            py[i] = ny;
        }
    }
}
