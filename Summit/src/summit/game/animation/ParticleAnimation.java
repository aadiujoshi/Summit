package summit.game.animation;

import summit.gfx.OrderPaintEvent;
import summit.gfx.PaintEvent;
import summit.gfx.Paintable;
import summit.gfx.RenderLayers;

public class ParticleAnimation extends ScheduledEvent implements Paintable{

    private int color;

    //dx is first 8 bits and dy is last 8 bits
    private int[] pVel;
    private long[] particles;

    private static final float gravity = -1;

    private int sx;
    private int sy;

    //x and y are pixel values
    public ParticleAnimation(int x, int y, int duration_ms, int particleCount) {
        super(1, duration_ms);

        pVel = new int[particleCount];
        particles = new long[particleCount];

        this.sx = x;
        this.sy = y;

        for(int i = 0; i < particles.length; i++) {
            pVel[i] = ((int)(Math.random()*11-5) << 8) | ((int)(Math.random()*5+1) << 0);
            particles[i] = (x << 16) | (y << 0);
        }
    }

    @Override
    public void setRenderLayer(OrderPaintEvent ope) {
        ope.addToLayer(RenderLayers.TILE_LAYER+1, this);
    }

    @Override
    public void paint(PaintEvent e) {
        for (int i = 0; i < particles.length; i++) {
            int x1 = (int)((particles[i] >> 16) & Integer.MAX_VALUE);
            int y1 = (int)((particles[i] >> 0) & Integer.MAX_VALUE);
            e.getRenderer().fillRect(x1, y1, 1, 1, color);
        }
    }

    @Override
    public void run() {
        for (int i = 0; i < particles.length; i++) {

            float ndx = 1;
            float ndy = 1;

            int nx = (int)(sx + ndx);
            int ny = (int)((particles[i] >> 0) & Integer.MAX_VALUE);
        }
    }
}
