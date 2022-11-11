package summit.gui.menu;

import summit.gfx.Renderer;
import summit.gfx.PaintEvent;

public class MenuButton extends MenuComponent{

    private String text;

    public MenuButton(String text, Menu parent, float relX, float relY, float relWidth, float relHeight) {
        super(parent, relX, relY, relWidth, relHeight);
        this.text = text;
    }

    @Override
    public void paint(PaintEvent e){
        super.paint(e);
    }
}
