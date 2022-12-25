package summit.game.entity.mob;

import summit.game.ai.IceKingAI;
import summit.gfx.Sprite;

public class IceKing extends HumanoidEntity{

    public IceKing(float x, float y) {
        super(x, y);
        super.setSprite(Sprite.ICE_KING_NEUTRAL);
        super.setAI(new IceKingAI(this));
    }
}