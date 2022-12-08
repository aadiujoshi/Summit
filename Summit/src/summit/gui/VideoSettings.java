package summit.gui;

import summit.gfx.PaintEvent;
import summit.gfx.Sprite;
import summit.util.Settings;

import java.awt.event.MouseEvent;

public class VideoSettings extends Container {

    public VideoSettings() {
        super(null, 0.5f, 0.5f, Sprite.FILL_SCREEN);
        
        // @Override
        //     public void guiClick(MouseEvent e){
        //         Settings.changeSetting(getGuiSprite(), e);
        //     }

        // TextContainer ao = new TextContainer("AMBIENT OCCLUSION", this, 0.1f, 0.1f, Sprite.MENUBOX2);
        // TextContainer vsync = new TextContainer("VSYNC", this, 0.1f, 0.1f, Sprite.MENUBOX2);
        // TextContainer threads = new TextContainer("RENDER THREADS", this, 0.1f, 0.1f, Sprite.MENUBOX2);

        TextContainer ao_count = new TextContainer("AMBIENT OCCLUSION", this, 0.1f, 0.1f, Sprite.MENUBOX3);
        TextContainer vsync_count = new TextContainer("VSYNC", this, 0.1f, 0.1f, Sprite.MENUBOX3);
        TextContainer threads_count = new TextContainer("RENDER THREADS", this, 0.1f, 0.1f, Sprite.MENUBOX3);

        TextContainer ao_inc = new TextContainer("AMBIENT OCCLUSION", this, 0.1f, 0.1f, Sprite.MENUBOX3){
            @Override
            public void guiClick(MouseEvent e){
                int n = Integer.parseInt(ao_count.getText());
                if(n + 1 <= 9){
                    ao_count.setText((n + 1)+"");
                    Settings.changeSetting("ambient_occlusion", (n+1));
                }
            }
        };

        TextContainer vsync_inc = new TextContainer("VSYNC", this, 0.1f, 0.1f, Sprite.MENUBOX3){
            @Override
            public void guiClick(MouseEvent e){
                int n = Integer.parseInt(ao_count.getText());
                if(n + 1 <= 9){
                    ao_count.setText((n + 1)+"");
                    Settings.changeSetting("ambient_occlusion", (n+1));
                }
            }
        };

        TextContainer threads_inc = new TextContainer("RENDER THREADS", this, 0.1f, 0.1f, Sprite.MENUBOX3);
    }

    @Override
    public void paint(PaintEvent e){
        paintComponents(e);
    }
}
