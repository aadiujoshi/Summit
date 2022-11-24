package summit.gui;

import summit.gfx.Renderer;
import summit.game.entity.mob.Player;
import summit.gfx.ColorFilter;
import summit.gfx.PaintEvent;
import summit.gfx.Sprite;

public class HUD extends Container{

    private Player player;
    
    public HUD(Player player) {
        super(null, 0.75f, 0.13f, 0.5f, 0.2f);
        this.player = player;
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
}
