/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.gui;

import summit.gfx.ColorFilter;
import summit.gfx.PaintEvent;
import summit.gfx.Sprite;

import java.awt.Color;
import java.awt.Desktop;
import java.net.URL;

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
                window.setState(WindowState.SAVEDGAMESELECTION);
            }
        };

        TextContainer quit = new TextContainer("QUIT", this, window, 0.75f, 0.5f, Sprite.MENUBOX2){
            @Override
            public void guiClick(MouseEvent e){
                if(window != null){
                    window.manualQuit();
                }
            }
        };

        TextContainer videoSettings = new TextContainer("SETTINGS", this, window, 0.5f, 0.7f, Sprite.MENUBOX6){
            @Override
            public void guiClick(MouseEvent e){
                window.setState(WindowState.SETTINGS);
            }
        };

        TextContainer email = new TextContainer("EMAIL", this, window, 0.89f, 0.85f, Sprite.MENUBOX6){
            @Override
            public void guiClick(MouseEvent e){
                try {
                    Desktop.getDesktop().browse(new URL("https://mail.google.com/mail/?view=cm&fs=1&to=aadiujoshi@gmail.com&su=&body=&bcc=").toURI());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        email.setTextFilter(new ColorFilter(Color.GREEN.getRGB()));
        email.setGuiSprite(null);
        
        addComponent(newGame);
        addComponent(savedGames);
        addComponent(quit);
        addComponent(videoSettings);
        addComponent(email);
    }
    
    @Override
    public void paint(PaintEvent e){
        super.paintComponents(e);
    }
}
