package summit.gui;

import summit.gfx.PaintEvent;
import summit.gfx.Renderer;
import summit.gfx.Sprite;

public class TransitionScreen extends TextContainer{

    public TransitionScreen(Window window, String text) {
        super(text, null, window, 0.5f, 0.5f, Sprite.FILL_SCREEN);
        super.setNavContainer(false);
    }

    @Override
    public void paint(PaintEvent e){
        e.getRenderer().fillRect(0, 0, Renderer.WIDTH, Renderer.HEIGHT, 0x254aff);
        super.paint(e);
    }
}
