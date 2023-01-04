package summit.gui;

import summit.gfx.ColorFilter;
import summit.gfx.Sprite;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class GameCreationMenu extends Container implements KeyListener{

    private TextContainer textbox;
    private TextContainer create;

    public GameCreationMenu(Window window) {
        super(null, window, 0.5f, 0.5f, Sprite.SUMMIT_BACKGROUND2);
        super.setOutline(false);

        this.textbox = new TextContainer("new game", this, window, 0.5f, 0.4f, Sprite.TEXTBOX1);
        textbox.setTextFilter(new ColorFilter(0xadd8e6));

        this.create = new TextContainer("Create", this, window, 0.5f, 0.55f, Sprite.MENUBOX4){
            @Override
            public void guiClick(MouseEvent e){
                window.createWorld(textbox.getText());
            }
        };

        addComponent(textbox);
        addComponent(create);
    }
    

    @Override
    public void keyPressed(KeyEvent e) {
        String text = textbox.getText();
        
        if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE && text.length() > 0){
            textbox.setText(text.substring(0, text.length()-1));
        } else {
            textbox.setText(text + e.getKeyChar());
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
    }

}
