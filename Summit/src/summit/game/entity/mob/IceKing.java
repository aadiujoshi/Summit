package summit.game.entity.mob;

import summit.game.ai.HostileMobAI;
import summit.game.ai.IceKingAI;
import summit.game.item.IceStaff;
import summit.gfx.Sprite;

public class IceKing extends HumanoidEntity{

    public IceKing(float x, float y) {
        super(x, y); 
        super.setMaxHealth(50);
        super.setHealth(getMaxHealth());
        super.setSprite(Sprite.ICE_KING_NEUTRAL);
        super.setEquipped(new IceStaff(this));
        super.setAI(new HostileMobAI(this));
    }
}