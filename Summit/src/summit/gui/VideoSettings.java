package summit.gui;

import summit.gfx.ColorFilter;
import summit.gfx.PaintEvent;
import summit.gfx.Renderer;
import summit.gfx.Sprite;
import summit.util.Settings;

import java.awt.Color;
import java.awt.event.MouseEvent;

public class VideoSettings extends TextContainer {

    public VideoSettings(Window window) {
        super("VIDEO SETTINGS", null, window, 0.5f, 0.25f, Sprite.FILL_SCREEN);
        
        TextContainer ao_count = new TextContainer(
                    Settings.getSetting("ambient_occlusion").toString(),
                    this, window, 0.5f, 0.25f, Sprite.MENUBOX3);

        TextContainer vsync_count = new TextContainer(
                    Settings.getSetting("vsync").toString(), 
                    this, window, 0.5f, 0.5f, Sprite.MENUBOX3);

        TextContainer threads_count = new TextContainer(
                    Settings.getSetting("threads").toString(), 
                    this, window, 0.5f, 0.75f, Sprite.MENUBOX3);

        //-----------------------------------------------------------------------
        //-----------------add---------------------------------------------------
        //-----------------------------------------------------------------------

        TextContainer ao_inc = new TextContainer("+", this, window, 0.625f, 0.25f, Sprite.MENUBOX3){
            public void guiClick(MouseEvent e){
                updateCont(ao_count, "ambient_occlusion", 1);
            }
        };

        TextContainer vsync_inc = new TextContainer("+", this, window, 0.625f, 0.5f, Sprite.MENUBOX3){
            public void guiClick(MouseEvent e){
                updateCont(vsync_count, "vsync", 1);
            }
        };

        TextContainer threads_inc = new TextContainer("+", this, window, 0.625f, 0.75f, Sprite.MENUBOX3){
            public void guiClick(MouseEvent e){
                updateCont(threads_count, "threads", 1);
            }
        };

        //-----------------------------------------------------------------------
        //-----------------subtract----------------------------------------------
        //-----------------------------------------------------------------------

        TextContainer ao_dec = new TextContainer("-", this, window, 0.75f, 0.25f, Sprite.MENUBOX3){
            public void guiClick(MouseEvent e){
                updateCont(ao_count, "ambient_occlusion", -1);
            }
        };

        TextContainer vsync_dec = new TextContainer("-", this, window, 0.75f, 0.5f, Sprite.MENUBOX3){
            public void guiClick(MouseEvent e){
                updateCont(vsync_count, "vsync", -1);
            }
        };

        TextContainer threads_dec = new TextContainer("-", this, window, 0.75f, 0.75f, Sprite.MENUBOX3){
            public void guiClick(MouseEvent e){
                updateCont(threads_count, "threads", -1);
            }
        };

        //-----------------------------------------------------------------------
        //-----------------------------------------------------------------------
        //-----------------------------------------------------------------------
        
        addComponent(ao_count);
        addComponent(vsync_count);
        addComponent(threads_dec);

        addComponent(ao_inc);
        addComponent(vsync_inc);
        addComponent(threads_inc);

        addComponent(ao_dec);
        addComponent(vsync_dec);
        addComponent(threads_dec);
    }

    @Override
    public void guiClick(MouseEvent e){
        super.getParentWindow().pushGameContainer(this);
    }

    private void updateCont(TextContainer c, String property, int add){
        int n = Integer.parseInt(c.getText()) + add;
        if(n >= 0 && n <= 9){
            c.setText((n)+"");
            Settings.changeSetting(property, n);
        }
    }


    @Override
    public void paint(PaintEvent e){
        if(!isPushed())
            e.getRenderer().render(getGuiSprite(), (int)getX(), (int)getY(), Renderer.NO_OP, getFilter());

        if(isPushed()){
            paintComponents(e);

            ColorFilter f = new ColorFilter(Color.RED.getRGB());

            e.getRenderer().renderText("AMBIENT OCCLUSION", 
                        (int)(Renderer.WIDTH*0.33), 
                        (int)(Renderer.HEIGHT*0.25), 
                        Renderer.NO_OP, f);

            e.getRenderer().renderText("VSYNC", 
                        (int)(Renderer.WIDTH*0.33), 
                        (int)(Renderer.HEIGHT*0.5), 
                        Renderer.NO_OP, f);
                        
            e.getRenderer().renderText("THREADS", 
                        (int)(Renderer.WIDTH*0.33), 
                        (int)(Renderer.HEIGHT*0.75), 
                        Renderer.NO_OP, f);
        }
    }
}
