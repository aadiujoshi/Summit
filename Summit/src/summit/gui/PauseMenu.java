package summit.gui;

import java.awt.Color;
import java.awt.event.MouseEvent;

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
        super.setNavContainer(false);

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

        long el = world.getElapsedTime();
        
        String min = ((el/60000) < 10) ? "0"+(el/60000) : (el/60000)+"";
        
        String sec = ((el/1000)%60 < 10) ? "0"+((el/1000)%60) : ((el/1000)%60)+"";
        
        int mil_t = (int)(el%1000);

        String milli = "";
        if((mil_t) < 10)
            milli = "00"+(mil_t);
        else if((mil_t) < 100)
            milli = "0"+(mil_t);
        else 
            milli = (mil_t)+"";

        e.getRenderer().renderText(min + ":" + sec + ":" + milli, 
                    (int)(Renderer.WIDTH/2), 
                    (int)(Renderer.WIDTH * 0.125), 
                    Renderer.NO_OP, new ColorFilter(Color.GRAY.getRGB()));
    }
}
