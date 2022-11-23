package summit.gui;

import summit.gfx.PaintEvent;

import java.awt.event.MouseEvent;

public class MainSelectionMenu extends Container{

    public MainSelectionMenu() {
        super(null, 0.5f, 0.6f, 0.7f, 0.4f);

        addComponent(new TextContainer("NEW GAME", this, 0.15f, 0.5f, 0.2f, 0.4f){
            @Override
            public void guiClick(MouseEvent e){
                getParentWindow().setState(WindowState.NEWGAME);
            }
        });
        addComponent(new TextContainer("SAVED GAMES", this, 0.5f, 0.5f, 0.2f, 0.4f){
            @Override
            public void guiClick(MouseEvent e){
                System.out.println("not ready yet :)");
            }
        });
        addComponent(new TextContainer("QUIT", this, 0.85f, 0.5f, 0.2f, 0.4f){
            @Override
            public void guiClick(MouseEvent e){
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
}
