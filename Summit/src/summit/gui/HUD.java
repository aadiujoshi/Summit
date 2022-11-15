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
        if(player == null) return;

        float h = Math.round(player.getHealth()*2)/2;

        int py = getY();
        // System.out.println(player.onFire());
        int rwidth = 100;//(int)(player.getMaxHealth()*10);
        int right_x = getX()+(rwidth/2);
        int left_x = getX()-(rwidth/2);

        for(int px = left_x; px <= right_x; px+=10) {
            if( h > 0 && h != 0.5) {
                e.getRenderer().render(Sprite.FULL_HEART, px, py, Renderer.NO_OP, null);
                h-=1;
            } else if(h == 0.5){
                e.getRenderer().render(Sprite.HALF_HEART, px, py, Renderer.NO_OP, null);
                h-=0.5;
            } else if(h == 0){
                e.getRenderer().render(Sprite.HALF_HEART, px, py, Renderer.NO_OP, null);
            }
        }
    }

    public void setPlayer(PlayerEntity player) {
        this.player = player;
    }
}
