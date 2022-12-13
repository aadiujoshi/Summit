package summit.game.animation;

import java.awt.Point;

import java.awt.geom.Point2D;
import java.util.Arrays;

import summit.gfx.ColorFilter;
import summit.gfx.Light;
import summit.gfx.OrderPaintEvent;
import summit.gfx.PaintEvent;
import summit.gfx.RenderLayers;
import summit.gfx.Renderer;
import summit.util.Scheduler;

public class GlistenAnimation extends Animation{

    private int color;

    private int[] calls;
    private Light[] lights;
    
    private ColorFilter light_filter;

    //origin
    private float sx;
    private float sy;

    private float spread = 0.75f;
    
    private static final int frame_len = 150;

    //duration in ms
    // delay * calls = duration
    public GlistenAnimation(float x, float y, int p_count, int color) {
        super(frame_len, p_count+1);

        this.sx = x;
        this.sy = y;

        this.light_filter = new ColorFilter(color);
        
        lights = new Light[p_count];
        calls = new int[p_count];

        for (int i = 0; i < lights.length; i++) {
            lights[i] = Light.NO_LIGHT;
        }

        for (int i = 0; i < calls.length; i++) {
            calls[i] = -i;
        }

        Scheduler.registerEvent(this);
    }

    @Override
    public void setRenderLayer(OrderPaintEvent e) {
        e.addToLayer(RenderLayers.TILE_LAYER+1, this);

        for (int i = 0; i < lights.length; i++) {
            lights[i].setRenderLayer(e);
        }
    }

    @Override
    public void paint(PaintEvent e) {
        for (int i = 0; i < lights.length; i++) {
            Point po = Renderer.toPixel(lights[i].getX(), lights[i].getY(), e.getCamera());

            if(calls[i] == 0){
                // System.out.println(calls[i]);
                // System.out.println(lights[i].getX() + lights[i].getY());
                e.getRenderer().fillRect(po.x, po.y, 1, 1, color);
            }
            if(calls[i] == 1){
                e.getRenderer().fillRect(po.x-1, po.y, 1, 1, color);
                e.getRenderer().fillRect(po.x, po.y-1, 1, 1, color);
                e.getRenderer().fillRect(po.x+1, po.y, 1, 1, color);
                e.getRenderer().fillRect(po.x, po.y+1, 1, 1, color);
            }
        }
    }

    @Override
    public void run() {

        // for (Light l : lights) {
        //     if(l != Light.NO_LIGHT)
        //         System.out.print(l + "   ");
        // }
        // System.out.println();

        for (int i = 0; i < lights.length; i++) {
            if(calls[i] == -1){
                float nx = sx + (float)(Math.random()*spread-spread);
                float ny = sy + (float)(Math.random()*spread-spread);

                lights[i] = new Light(nx, ny, 0.25f, light_filter);
            }
            if(calls[i] != 0 && calls[i] != 1 && calls[i] != -1){
                lights[i] = Light.NO_LIGHT;
            }

            calls[i]++;
        }
    }
}


        // this.p1 = new Point2D.Float[]{
        //     new Point2D.Float(sx + (float)((Math.random()*2-1)/2), 
        //                       sy + (float)((Math.random()*2-1)/2)), 
        //     new Point2D.Float(sx + (float)((Math.random()*2-1)/2), 
        //                       sy + (float)((Math.random()*2-1)/2))};
        // this.p2 = new Point2D.Float[]{
        //     new Point2D.Float(sx + (float)((Math.random()*2-1)/2), 
        //                       sy + (float)((Math.random()*2-1)/2)), 
        //     new Point2D.Float(sx + (float)((Math.random()*2-1)/2), 
        //                       sy + (float)((Math.random()*2-1)/2))};
        // this.p3 = new Point2D.Float[]{
        //     new Point2D.Float(sx + (float)((Math.random()*2-1)/2), 
        //                       sy + (float)((Math.random()*2-1)/2)), 
        //     new Point2D.Float(sx + (float)((Math.random()*2-1)/2), 
        //                       sy + (float)((Math.random()*2-1)/2))};
