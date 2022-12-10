/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.gui;

import summit.gfx.PaintEvent;
import summit.gfx.Sprite;

import java.awt.event.MouseEvent;

public class MainSelectionMenu extends Container{

    public MainSelectionMenu(Window window) {
        super(null, window, 0.5f, 0.6f, Sprite.FILL_SCREEN);
        
        TextContainer newGame = new TextContainer("NEW GAME", this, window, 0.25f, 0.5f, Sprite.MENUBOX2){
            @Override
            public void guiClick(MouseEvent e){
                window.setState(WindowState.NEWGAME);
            }
        };
        
        TextContainer savedGames = new TextContainer("SAVED GAMES", this, window, 0.5f, 0.5f, Sprite.MENUBOX2){
            @Override
            public void guiClick(MouseEvent e){
                window.setState(WindowState.SAVEDGAME);
            }
        };

        TextContainer quit = new TextContainer("QUIT", this, window, 0.75f, 0.5f, Sprite.MENUBOX2){
            @Override
            public void guiClick(MouseEvent e){
                if(window != null){
                    window.quit();
                }
            }
        };

        newGame.setGuiSprite(Sprite.MENUBOX2);
        savedGames.setGuiSprite(Sprite.MENUBOX2);
        quit.setGuiSprite(Sprite.MENUBOX2);

        addComponent(newGame);
        addComponent(savedGames);
        addComponent(quit);
    }
    
    @Override
    public void paint(PaintEvent e){
        super.paintComponents(e);
    }
}
