package summit.gui.menu;

import summit.gfx.Renderer;
import summit.gui.Container;

import java.util.ArrayList;
import java.util.List;

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

        List<String> lines = new ArrayList<>();
        
        int k = 0;

        lines.add("");

        for(int i = 0; i < text.length(); i++){
            if(text.charAt(i) == ' '){
                k++;
                lines.add("");
                continue;
            }

            lines.set(k, lines.get(k)+text.charAt(i));
        }

        for (int i = 0; i < lines.size(); i++) {
            e.getRenderer().renderText(lines.get(i), getX(), getY()+(i*9)-(lines.size()/2*4), Renderer.NO_OP, null);
        }
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
