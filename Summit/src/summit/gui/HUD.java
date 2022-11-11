package summit.gui;

import summit.game.entity.PlayerEntity;
import summit.gfx.PaintEvent;
import summit.gui.menu.Container;

public class HUD extends Container{

    PlayerEntity player;

    public HUD(Container parent, float relX, float relY, float relWidth, float relHeight) {
        super(parent, relX, relY, relWidth, relHeight);
    }

    
    @Override
    public void paint(PaintEvent e){
        //draw health of player
    }

    public void setPlayer(PlayerEntity player) {
        this.player = player;
    }
}
