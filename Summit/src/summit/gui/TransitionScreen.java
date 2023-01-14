package summit.gui;

import summit.gfx.PaintEvent;
import summit.gfx.Renderer;
import summit.gfx.Sprite;

/**
 * A TransitionScreen is a non-navigable container that displays a message over
 * a solid colored background. The TransitionScreen is typically used to convey
 * a change in the application state, such as loading or transitioning between
 * menus.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R. S.
 *
 */
public class TransitionScreen extends TextContainer {

    /**
     * Constructs a new TransitionScreen with the specified text, window, and
     * background color.
     *
     * @param window the parent window of this TransitionScreen
     * @param text   the text to be displayed on the TransitionScreen
     */
    public TransitionScreen(Window window, String text) {
        super(text, null, window, 0.5f, 0.5f, Sprite.SUMMIT_BACKGROUND2);
        super.setNavContainer(false);
        super.setSplit(false);
        super.setOutline(false);
    }


    
    /** 
     * @param e
     */
    @Override
    public void paint(PaintEvent e) {
        // e.getRenderer().fillRect(0, 0, Renderer.WIDTH, Renderer.HEIGHT, 0x254aff);
        super.paint(e);
    }
}
