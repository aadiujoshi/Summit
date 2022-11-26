package summit.game.ai;

import summit.game.GameUpdateEvent;
import summit.game.entity.Entity;
import summit.game.entity.mob.Player;
import summit.util.Time;

public class HostileMobAI implements EntityAI{
    @Override
    public void next(GameUpdateEvent e, Entity ent) {

        Player p = e.getMap().getPlayer();

        float dest_x = p.getX();
        float dest_y = p.getY();

        float delta_x = ent.getDx()/Time.NS_IN_S * e.getDeltaTimeNS();
        float delta_y = ent.getDy()/Time.NS_IN_S * e.getDeltaTimeNS();

        delta_x *= (dest_x < ent.getX()) ? -1 : 1;
        delta_y *= (dest_y < ent.getY()) ? -1 : 1; 

        int reached = 0;

        if(Math.abs(ent.getX() - dest_x) >= 0.5f && ent.moveTo(e.getMap(), ent.getX() + delta_x, ent.getY()))
            ent.setX(ent.getX() + delta_x);
        else    
            reached++;
        
        if(Math.abs(ent.getY() - dest_y) >= 0.5f && ent.moveTo(e.getMap(), ent.getX(), ent.getY() + delta_y))
            ent.setY(ent.getY() + delta_y);
        else
            reached++;

        if(reached == 2){
            p.damage(ent.getHitDamage(), ent);
        }
    }
}
