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
import java.net.URI;
import java.net.URL;

import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

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

        TextContainer settings = new TextContainer("SETTINGS", this, window, 0.5f, 0.7f, Sprite.MENUBOX6){
            @Override
            public void guiClick(MouseEvent e){
                window.setState(WindowState.SETTINGS);
            }
        };

        TextContainer email = new TextContainer("EMAIL", this, window, 0.89f, 0.77f, Sprite.MENUBOX6){
            @Override
            public void guiClick(MouseEvent e){
                try {
                    Desktop.getDesktop().browse(new URL("https://mail.google.com/mail/?view=cm&fs=1&to=aadiujoshi@gmail.com&su=Summit+report&body=(Please+include+crash+report+files+as+well+as+recent+server+logs)&bcc=").toURI());
                    // Desktop.getDesktop().mail(new URI("mailto:aadiujoshi@gmail.com?subject=\"Summit crash report\""));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        email.setTextFilter(new ColorFilter(Color.GREEN.getRGB()));
        email.setGuiSprite(null);

        TextContainer help = new TextContainer("?", this, window, 0.7f, 0.7f, Sprite.MENUBOX5){
            @Override
            public void guiClick(MouseEvent e){
                try {
                    Desktop.getDesktop().open(new File(System.getProperty("user.dir")));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        };
        help.setTextFilter(new ColorFilter(0x00ff00));
        
        addComponent(newGame);
        addComponent(savedGames);
        addComponent(quit);
        addComponent(settings);
        addComponent(email);
        addComponent(help);
    }
    
    
    /** 
     * @param e
     */
    @Override
    public void paint(PaintEvent e){
        super.paintComponents(e);
    }
}
