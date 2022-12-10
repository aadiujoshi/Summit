/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.gui;

import summit.gfx.Renderer;

import java.util.ArrayList;
import java.util.List;

import summit.gfx.ColorFilter;
import summit.gfx.PaintEvent;

public class TextContainer extends Container{

    private String text;
    private ColorFilter textFilter;

    public TextContainer(String text, Container parent, 
                        Window window, 
                        float relX, float relY, 
                        String sprite) {
        super(parent, window, relX, relY, sprite);
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
            e.getRenderer().renderText(lines.get(i), (int)getX(), (int)getY()+(i*9)-(lines.size()/2*4), Renderer.NO_OP, textFilter);
        }
    }
    
    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }
    
    public ColorFilter getTextFilter() {
        return this.textFilter;
    }

    public void setTextFilter(ColorFilter textFilter) {
        this.textFilter = textFilter;
    }
}
