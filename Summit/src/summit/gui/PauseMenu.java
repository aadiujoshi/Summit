package summit.gui;

import java.awt.event.MouseEvent;

import summit.gfx.Sprite;

public class PauseMenu extends Container{

    public PauseMenu(Window parentWindow) {
        super(null, 0.5f, 0.5f, Sprite.FILL_SCREEN);

        // addComponent(new VideoSettings());

        // addComponent(new TextContainer("QUIT", this, getY(), getX(), getWidth(), getHeight()){
        //     @Override
        //     public void guiClick(MouseEvent e){
        //         super.guiClick(e);
        //     }
        // });
    }
}
