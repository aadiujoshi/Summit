package summit.gui;

import java.awt.event.MouseEvent;

import summit.Main;
import summit.game.GameWorld;
import summit.gfx.Sprite;
import summit.util.GameLoader;
import summit.util.Sound;

public class PauseMenu extends Container{

    private VideoSettings vs;
    private GameWorld world;

    public PauseMenu(Window window, GameWorld world) {
        super(null, window, 0.5f, 0.5f, Sprite.FILL_SCREEN);

        vs = new VideoSettings(window);
        this.world = world;

        addComponent(new TextContainer("VIDEO SETTINGS", vs, window, 0.75f, 0.5f, Sprite.MENUBOX2){
            @Override
            public void guiClick(MouseEvent e){
                window.pushGameContainer(vs);
            }
        });

        addComponent(new TextContainer("SAVE QUIT", this, window, 0.25f, 0.5f, Sprite.MENUBOX2){
            @Override
            public void guiClick(MouseEvent e){
                if(window != null){
                    Sound.stopAll();
                    world.terminate();
                    window.transition(new TransitionScreen(window, "Saving world..."));
                    GameLoader.saveWorld(world);
                    window.endTransition(WindowState.BACK);
                }
            }
        });

        addComponent(new TextContainer("RETURN", this, window, 0.5f, 0.5f, Sprite.MENUBOX2){
            @Override
            public void guiClick(MouseEvent e){
                window.popGameContainer();
            }
        });
    }

    @Override
    public void close(){
        world.unpause();
    }
}
