package summit.gui;

import java.awt.Color;
import java.awt.event.MouseEvent;

import summit.Main;
import summit.game.GameWorld;
import summit.gfx.ColorFilter;
import summit.gfx.PaintEvent;
import summit.gfx.Renderer;
import summit.gfx.Sprite;
import summit.util.GameLoader;
import summit.util.Sound;

public class PauseMenu extends Container{

    private SettingsGUI vs;
    private GameWorld world;

    public PauseMenu(Window window, GameWorld world) {
        super(null, window, 0.5f, 0.5f, Sprite.FILL_SCREEN);

        vs = new SettingsGUI(window);
        this.world = world;

        addComponent(new TextContainer("SETTINGS", vs, window, 0.75f, 0.5f, Sprite.MENUBOX2){
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
                    window.endTransition();
                    window.setState(WindowState.SELECTIONMENUS);
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

    @Override
    public void paint(PaintEvent e){
        super.paint(e);

        e.getRenderer().renderText(world.getElapsedTime()+"", 
                    (int)(Renderer.WIDTH/2), 
                    (int)(Renderer.WIDTH * 0.125), 
                    Renderer.NO_OP, new ColorFilter(Color.BLUE.getRGB()));
    }
}
