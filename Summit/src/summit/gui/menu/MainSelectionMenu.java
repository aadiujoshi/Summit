package summit.gui.menu;

import summit.gfx.PaintEvent;
import summit.gui.Window;

import java.awt.event.MouseEvent;

public class MainSelectionMenu extends Container{

    public MainSelectionMenu() {
        super(null, 0.5f, 0.6f, 0.7f, 0.4f);
        addComponent(new TextContainer("NEW GAME", this, 0.15f, 0.5f, 0.2f, 0.4f){
            @Override
            public void guiClick(MouseEvent e){

            }
        });
        addComponent(new TextContainer("SAVED GAMES", this, 0.5f, 0.5f, 0.2f, 0.4f){
            @Override
            public void guiClick(MouseEvent e){
                
            }
        });
        addComponent(new TextContainer("QUIT",this, 0.85f, 0.5f, 0.2f, 0.4f){
            @Override
            public void guiClick(MouseEvent e){
                System.out.println(getParentWindow());
                if(getParentWindow() != null){
                    getParentWindow().quit();
                }
            }
        });
    }
    
    @Override
    public void paint(PaintEvent e){
        // super.paint(e);
        super.paintComponents(e);
    }

    @Override
    public void setParentWindow(Window window){
        super.setParentWindow(window);
        getComponent(0).setParentWindow(window);
        getComponent(1).setParentWindow(window);
        getComponent(2).setParentWindow(window);

    }
}
