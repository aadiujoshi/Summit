package summit.gui;

import summit.gfx.Renderer;
import summit.game.entity.mob.PlayerEntity;
import summit.gfx.PaintEvent;
import summit.gfx.Sprite;
import summit.gui.menu.Container;

public class HUD extends Container{

    PlayerEntity player;
    
    public HUD() {
        super(null, 0.3f, 0.8f, 0.5f, 0.2f);
    }
    
    @Override
    public void paint(PaintEvent e){
        
        //round to nearest 0.5
        float h = Math.round(player.getHealth()*2)/2;

        int py = getY();
        int right_x = (int)(getX()+(player.getMaxHealth()*10/2));
        int left_x = (int)(getX()-(player.getMaxHealth()*10/2));
        
        for(int px = left_x; px < right_x; px+=10) {
            if(h == 0){
                e.getRenderer().render(Sprite.EMPTY_HEART, px, py, Renderer.NO_OP, null);
            } else if(h == 0.5){
                e.getRenderer().render(Sprite.HALF_HEART, px, py, Renderer.NO_OP, null);
                h-=0.5;
            } else if(h % 1 == 0){
                e.getRenderer().render(Sprite.FULL_HEART, px, py, Renderer.NO_OP, null);
                h-=1;
            }
        }
    }

    public void setPlayer(PlayerEntity player) {
        this.player = player;
    }
}
