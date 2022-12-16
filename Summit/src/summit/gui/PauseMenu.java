package summit.gui;

import java.awt.event.MouseEvent;

import summit.Main;
import summit.game.GameWorld;
import summit.gfx.Sprite;
import summit.util.GameLoader;

public class PauseMenu extends Container{

    private VideoSettings vs;
    private GameWorld world;

    public PauseMenu(Window window, GameWorld world) {
        super(null, window, 0.5f, 0.5f, Sprite.FILL_SCREEN);

        vs = new VideoSettings(window);
        this.world = world;

        addComponent(new TextContainer("VIDEO SETTINGS", vs, window, getWidth(), getHeight(), getGuiSprite()){
            @Override
            public void guiClick(MouseEvent e){
                window.pushGameContainer(vs);
            }
        });

        addComponent(new TextContainer("SAVE AND QUIT", this, window, 0.25f, 0.5f, Sprite.MENUBOX2){
            @Override
            public void guiClick(MouseEvent e){
                if(window != null){
                    GameLoader.saveWorld(world, "\\gamesaves\\testsave1.txt");
                    window.setState(WindowState.SELECTIONMENUS);
                }
            }
        });

        addComponent(new TextContainer("RETURN", this, window, 0.5f, 0.5f, Sprite.MENUBOX2){
            @Override
            public void guiClick(MouseEvent e){
                super.guiClick(e);
            }
        });
    }

    @Override
    public void close(){
        world.unpause();
    }
}
