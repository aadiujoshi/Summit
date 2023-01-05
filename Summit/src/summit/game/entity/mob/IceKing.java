package summit.game.entity.mob;

import summit.game.GameUpdateEvent;
import summit.game.GameWorld;
import summit.game.ai.HostileMobAI;
import summit.game.ai.IceKingAI;
import summit.game.item.IceStaff;
import summit.gfx.Sprite;

/**
 * 
 * IceKing is a HumanoidEntity that represents the Ice King enemy in the game.
 * It extends HumanoidEntity and has an additional destroy method that sets the
 * completion state of the game to
 * victory when the IceKing is destroyed. It also has a HostileMobAI and is
 * equipped with an IceStaff.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R.
 */
public class IceKing extends HumanoidEntity {

    /**
     * 
     * Constructs a new IceKing with the specified x and y position. The IceKing has
     * a maximum health of 50 and is initially
     * set to that value. It has the appropriate sprite states for the IceKing
     * character and is equipped with an IceStaff.
     * Its AI is set to a HostileMobAI.
     * 
     * @param x the x position of the IceKing
     * @param y the y position of the IceKing
     */
    public IceKing(float x, float y) {
        super(x, y);
        super.setMaxHealth(50);
        super.setHealth(getMaxHealth());
        super.setSpriteStates(Sprite.ICE_KING_NEUTRAL, Sprite.ICE_KING, Sprite.ICE_KING_NEUTRAL);
        super.setEquipped(new IceStaff(this));
        super.setAI(new HostileMobAI(this));
    }

    /**
     * 
     * Calls the destroy method of the superclass and sets the completion state of
     * the game to victory.
     * 
     * @param e the GameUpdateEvent used to access the current GameWorld
     */
    @Override
    public void destroy(GameUpdateEvent e) {
        super.destroy(e);
        e.getWorld().setCompletion(GameWorld.GAME_VICTORY);
    }
}