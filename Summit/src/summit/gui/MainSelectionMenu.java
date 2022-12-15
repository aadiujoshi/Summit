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

        TextContainer videoSettings = new TextContainer("SETTINGS", this, window, 0.5f, 0.7f, Sprite.MENUBOX6){
            @Override
            public void guiClick(MouseEvent e){
                window.setState(WindowState.SETTINGS);
            }
        };
        
        addComponent(newGame);
        addComponent(savedGames);
        addComponent(quit);
        addComponent(videoSettings);
    }
    
    @Override
    public void paint(PaintEvent e){
        super.paintComponents(e);
    }
}
