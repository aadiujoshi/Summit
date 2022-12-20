package summit.game.animation;

import summit.gfx.OrderPaintEvent;
import summit.gfx.PaintEvent;
import summit.gfx.RenderLayers;
import summit.gfx.Renderer;
import summit.util.Region;

public class TransitionAnimation extends Animation {

    private int radius;

    public TransitionAnimation() {
        super(4, (int)Region.distance(Renderer.WIDTH/2, Renderer.HEIGHT/2, 0, 0)+5);

        radius = 0;
    }

    @Override
    public void setRenderLayer(OrderPaintEvent e) {
        e.addToLayer(RenderLayers.TOP_LAYER, this);
    }

    @Override
    public void paint(PaintEvent e) {
        int[][] frame = e.getRenderer().getFrame();

        for (int r = 0; r < frame.length; r++) {
            for (int c = 0; c < frame[0].length; c++) {
                if(Region.distance(c, r, Renderer.WIDTH/2, Renderer.HEIGHT/2) >= radius){
                    // frame[r][c] = Renderer.toIntRGB((int)(Math.random()*256), 
                    //                                 (int)(Math.random()*256), 
                    //                                 (int)(Math.random()*256));
                    frame[r][c] = 0;
                }
            }
        }
    }

    @Override
    public void run() {
        radius++;
    }
}
