package summit.gui.menu;

import summit.gfx.Renderer;
import summit.gfx.PaintEvent;

public class TextContainer extends Container{

    private String text;

    public TextContainer(String text, Container parent, float relX, float relY, float relWidth, float relHeight) {
        super(parent, relX, relY, relWidth, relHeight);
        this.text = text;
    }

    @Override
    public void paint(PaintEvent e){
        super.paint(e);
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
