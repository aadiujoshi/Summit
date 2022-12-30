package summit.gui;

import summit.game.GameWorld;
import summit.gfx.PaintEvent;
import summit.gfx.Renderer;
import summit.gfx.Sprite;

import java.awt.event.MouseEvent;

public class PauseButton extends Container{

    private PauseMenu pm;

    private GameWorld world;

    public PauseButton(Window window, GameWorld world) {
        super(null, window, 0.96f, 0.08f, Sprite.PAUSE_ICON);
        pm = new PauseMenu(window, world);
        this.world = world;
        super.setNavContainer(false);
        addComponent(pm);
    }

    @Override
    public void paint(PaintEvent e){
        e.getRenderer().render(getGuiSprite(), (int)getX(), (int)getY(), Renderer.NO_OP, getFilter());
    }

    @Override
    public void guiClick(MouseEvent e){
        world.pause();
        super.getParentWindow().pushGameContainer(pm);
    }
}
