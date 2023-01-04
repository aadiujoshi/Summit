package summit.game.entity.mob;

import summit.game.GameUpdateEvent;
import summit.game.GameWorld;
import summit.game.ai.HostileMobAI;
import summit.game.ai.IceKingAI;
import summit.game.item.IceStaff;
import summit.gfx.Sprite;

public class IceKing extends HumanoidEntity{

    public IceKing(float x, float y) {
        super(x, y); 
        super.setMaxHealth(50);
        super.setHealth(getMaxHealth());
        super.setSpriteStates(Sprite.ICE_KING_NEUTRAL, Sprite.ICE_KING, Sprite.ICE_KING_NEUTRAL);
        super.setEquipped(new IceStaff(this));
        super.setAI(new HostileMobAI(this));
    }

    @Override
    public void destroy(GameUpdateEvent e){
        super.destroy(e);
        e.getWorld().setCompletion(GameWorld.GAME_VICTORY);
    }
}