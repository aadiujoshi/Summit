/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.gui;

import summit.gfx.Renderer;
import summit.game.entity.mob.Player;
import summit.gfx.ColorFilter;
import summit.gfx.OrderPaintEvent;
import summit.gfx.PaintEvent;
import summit.gfx.RenderLayers;
import summit.gfx.Sprite;
import summit.util.ScheduledEvent;
import summit.util.Scheduler;

import java.awt.event.MouseEvent;

public class HUD extends Container{

    private Player player;
    
    private String message;
    private ScheduledEvent messageTimer;

    public HUD(Player player) {
        super(null, null, 0.5f, 0.5f, Sprite.FILL_SCREEN);
        this.player = player;
        this.message = "";
    }

    @Override
    public void guiClick(MouseEvent e){
    }

    @Override
    public void setRenderLayer(OrderPaintEvent e){
        super.setRenderLayer(e);
        
        e.addToLayer(RenderLayers.TOP_LAYER, this);
    }

    @Override
    public void paint(PaintEvent e){

        Renderer ren = e.getRenderer();

        ///////////////////////////////////////////////////////////////////////////////////

        ren.renderText(("x:" + (Math.round(player.getX()))), 
                    20, 15, Renderer.NO_OP, new ColorFilter(255, 0, 0));

        ren.renderText(("y:" + (Math.round(player.getY()))), 
                20, 25, Renderer.NO_OP, new ColorFilter(255, 0, 0));  

        ///////////////////////////////////////////////////////////////////////////////////

        //round to nearest 0.5
        float h = Math.round(player.getHealth()*2)/2;

        {
            int py = (int)(getHeight()*0.13);
            int right_x = (int)((getWidth()*0.75)+(player.getMaxHealth()*10/2));
            int left_x = (int)((getWidth()*0.75)-(player.getMaxHealth()*10/2));
            
            for(int px = left_x; px < right_x; px+=10) {
                if(h == 0){
                    ren.render(Sprite.EMPTY_HEART, px, py, Renderer.NO_OP, ColorFilter.NOFILTER);
                } else if(h == 0.5){
                    ren.render(Sprite.HALF_HEART, px, py, Renderer.NO_OP, ColorFilter.NOFILTER);
                    h-=0.5;
                } else if(h % 1 == 0){
                    ren.render(Sprite.FULL_HEART, px, py, Renderer.NO_OP, ColorFilter.NOFILTER);
                    h-=1;
                }
            }
        }

        ///////////////////////////////////////////////////////////////////////////////////

        boolean[] keys = player.getObtainedKeys();

        int start_x = (int)(Renderer.WIDTH*0.25);
        int key_y = (int)(Renderer.HEIGHT*0.13);

        for (int i = 0; i < keys.length; i++) {
            int _sx = (start_x + (i*24));

            if(keys[i]){
                if(i == 0)
                    ren.render(Sprite.RED_KEY, _sx, key_y, Renderer.NO_OP, ColorFilter.NOFILTER);
                if(i == 1)
                    ren.render(Sprite.GREEN_KEY, _sx, key_y, Renderer.NO_OP, ColorFilter.NOFILTER);
                if(i == 2)
                    ren.render(Sprite.BLUE_KEY, _sx, key_y, Renderer.NO_OP, ColorFilter.NOFILTER);
            } else {
                ren.render(Sprite.GREY_KEY, _sx, key_y, Renderer.NO_OP, ColorFilter.NOFILTER);
            }
        }

        e.getRenderer().renderText(message, (int)getX(), (int)(getHeight()*0.2f), Renderer.NO_OP, new ColorFilter(0xffffff));
    }
    
    public void setMessage(String m) {
        this.message = m;

        if(messageTimer != null){
            this.messageTimer.manualTerminate();
        }
        
        this.messageTimer = new ScheduledEvent(2000, 1) {
            @Override
            public void run() {
                message = "";
            }
        };

        Scheduler.registerEvent(messageTimer);
    }
}
