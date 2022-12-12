package summit.game.animation;

import java.awt.Point;

import java.awt.geom.Point2D;

import summit.gfx.OrderPaintEvent;
import summit.gfx.PaintEvent;
import summit.gfx.RenderLayers;
import summit.gfx.Renderer;
import summit.util.Scheduler;

public class GlistenAnimation extends Animation{

    private int color;

    private float sx;
    private float sy;
    
    private float px;
    private float py;

    //fix this lol (suggest make even and odd chcking)
    private Point2D.Float p1[];
    private Point2D.Float p2[];
    private Point2D.Float p3[];

    private int completedFrames;

    private static int frame_len = 150;

    //duration in ms
    // delay * calls = duration
    public GlistenAnimation(float x, float y, int duration, int color) {
        super(frame_len, duration/(frame_len));
        this.sx = x;
        this.sy = y;

        this.p1 = new Point2D.Float[]{
            new Point2D.Float(sx + (float)((Math.random()*2-1)/2), 
                              sy + (float)((Math.random()*2-1)/2)), 
            new Point2D.Float(sx + (float)((Math.random()*2-1)/2), 
                              sy + (float)((Math.random()*2-1)/2))};
        this.p2 = new Point2D.Float[]{
            new Point2D.Float(sx + (float)((Math.random()*2-1)/2), 
                              sy + (float)((Math.random()*2-1)/2)), 
            new Point2D.Float(sx + (float)((Math.random()*2-1)/2), 
                              sy + (float)((Math.random()*2-1)/2))};
        this.p3 = new Point2D.Float[]{
            new Point2D.Float(sx + (float)((Math.random()*2-1)/2), 
                              sy + (float)((Math.random()*2-1)/2)), 
            new Point2D.Float(sx + (float)((Math.random()*2-1)/2), 
                              sy + (float)((Math.random()*2-1)/2))};

        this.px = sx + (float)((Math.random()*2-1)/2);
        this.py = sy + (float)((Math.random()*2-1)/2);

        Scheduler.registerEvent(this);
    }

    @Override
    public void setRenderLayer(OrderPaintEvent e) {
        e.addToLayer(RenderLayers.TILE_LAYER+1, this);
    }

    @Override
    public void paint(PaintEvent e) {
        Point po = Renderer.toPixel(px, py, e.getCamera());

        if(completedFrames == 0)
            e.getRenderer().fillRect(po.x, po.y, 1, 1, color);
        if(completedFrames == 1){
            e.getRenderer().fillRect(po.x-1, po.y, 1, 1, color);
            e.getRenderer().fillRect(po.x, po.y-1, 1, 1, color);
            e.getRenderer().fillRect(po.x+1, po.y, 1, 1, color);
            e.getRenderer().fillRect(po.x, po.y+1, 1, 1, color);
        }
    }

    @Override
    public void run() {
        if(completedFrames == 2){
            completedFrames = 0;
            this.px = sx + (float)((Math.random()*2-1)/2);
            this.py = sy + (float)((Math.random()*2-1)/2);
            return;
        }

        completedFrames++;
    }
}
