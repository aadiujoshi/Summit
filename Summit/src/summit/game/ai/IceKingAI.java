package summit.game.ai;

import summit.game.GameUpdateEvent;
import summit.game.entity.Entity;

/**
 * 
 * Controls the movement of the Ice King.
 * 
 * This class extends EntityAI and is used to control the movement of an Entity
 * object that represents the Ice King.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R.
 */
public class IceKingAI extends EntityAI {

    /**
     * 
     * Constructs a new IceKingAI for the specified Entity.
     * 
     * @param e the Entity object that this IceKingAI will control
     */
    public IceKingAI(Entity e) {
        super(e);
    }

    /**
     * 
     * Updates the Ice King's movement.
     * This method should be called every game tick to update the Ice King's
     * position.
     * 
     * @param e the GameUpdateEvent for the current game tick
     * @see GameUpdateEvent
     */
    @Override
    public void next(GameUpdateEvent e) {

    }

    /**
     * 
     * Re-initializes the IceKingAI.
     * This method should be called when the Ice King is reset or re-spawned.
     */
    @Override
    public void reinit() {

    }
}