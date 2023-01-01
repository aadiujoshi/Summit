
package summit.gui;

import summit.gfx.Renderer;

import java.util.ArrayList;
import java.util.List;

import summit.gfx.ColorFilter;
import summit.gfx.PaintEvent;

/**
 * This class represents a container for displaying text. 
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R. S.
 */
public class TextContainer extends Container {

    private String text;
    private ColorFilter textFilter;

    /**
     * Constructs a TextContainer with the given text, parent container, window,
     * relative x and y position, and sprite.
     * 
     * @param text   the text to be displayed
     * @param parent the parent container of this container
     * @param window the window that this container belongs to
     * @param relX   the relative x position of this container
     * @param relY   the relative y position of this container
     * @param sprite the sprite to be used for this container
     */
    public TextContainer(String text, Container parent,
            Window window,
            float relX, float relY,
            String sprite) {
        super(parent, window, relX, relY, sprite);
        this.text = text;
        textFilter = new ColorFilter(0xffffff);
    }

    @Override
    public void paint(PaintEvent e) {
        super.paint(e);

        List<String> lines = new ArrayList<>();

        int k = 0;

        lines.add("");

        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == ' ') {
                k++;
                lines.add("");
                continue;
            }

            lines.set(k, lines.get(k) + text.charAt(i));
        }

        for (int i = 0; i < lines.size(); i++) {
            e.getRenderer().renderText(lines.get(i), (int) getX(), (int) getY() + (i * 9) - (lines.size() / 2 * 4),
                    Renderer.NO_OP, textFilter);
        }
    }

    /**
     * Returns the text displayed by this container.
     * 
     * @return the text displayed by this container
     */
    public String getText() {
        return this.text;
    }

    /**
     * Sets the text displayed by this container.
     * 
     * @param text the text to be displayed by this container
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Returns the color filter applied to the text.
     * 
     * @return the color filter applied to the text
     */
    public ColorFilter getTextFilter() {
        return this.textFilter;
    }

    /**
     * Sets the color filter applied to the text.
     * 
     * @param textFilter the color filter to be applied to the text
     */
    public void setTextFilter(ColorFilter textFilter) {
        this.textFilter = textFilter;
    }
}
