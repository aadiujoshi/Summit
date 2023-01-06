package summit.game.ai;

import summit.game.GameUpdateEvent;
import summit.game.entity.mob.MobEntity;
import summit.game.entity.mob.Player;

/**
 * 
 * Controls the movement of a HostileMobEntity.
 * 
 * This class extends EntityAI and is used to control the movement of a
 * MobEntity object that is hostile towards the player. The MobEntity will
 * wander around randomly until it is within a certain range of the player, at
 * which point it will start moving towards the player. If the MobEntity is
 * within attacking range of the player, it will attack the player.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R.
 */
public class HostileMobAI extends EntityAI {

    /** The range at which the MobEntity will start moving towards the player. */
    private float interestRange = 10;

    /**
     * 
     * Constructs a new HostileMobAI for the specified MobEntity.
     * 
     * @param e the MobEntity object that this HostileMobAI will control
     */
    public HostileMobAI(MobEntity e) {
        super(e);
    }

    /**
     * 
     * Updates the MobEntity's movement and attack behavior.
     * 
     * This method should be called every game tick to update the MobEntity's
     * position and attack behavior. If the player is outside of the MobEntity's
     * interest range or if the player is not
     * within the MobEntity's line of sight, the MobEntity will wander around
     * randomly. If the player is within the
     * MobEntity's interest range and line of sight, the MobEntity will move towards
     * the player. If the MobEntity
     * is within attacking range of theplayer, it will attack the player.
     * 
     * @param e the GameUpdateEvent for the current game tick
     * 
     * @see GameUpdateEvent
     */
    @Override
    public void next(GameUpdateEvent e) {
        super.next(e);

        // attack range of the equipped weapon
        float attackRange = entity.getAttackRange();

        Player p = e.getMap().getPlayer();

        float dist = entity.distance(p);
        boolean los = entity.lineOfSight(p, e.getMap());

        // System.out.println(dist + " " + e);

        if (dist > interestRange || !los) {
            super.wander(true);
            // System.out.println("WAndering");
            return;
        } else if (dist <= interestRange && dist > attackRange - 1 && los) {
            setDest(p.getX(), p.getY());
            super.wander(false);
            // System.out.println("going to attacking position");
        }

        if (dist <= attackRange - 1) {
            super.wander(true);
            // // System.out.println("reached attack position");
        }

        if (super.reachedDest()) {
            entity.attack(p.getX(), p.getY(), e);
            // System.out.println("reached attack position");
        }
    }

    @Override
    public void reinit() {

    }
}
