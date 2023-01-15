package summit.game.ai;

import java.io.Serializable;

import summit.game.GameUpdateEvent;
import summit.game.entity.Entity;
import summit.util.GameScheduler;
import summit.util.ScheduledEvent;
import summit.util.Time;

/**
 * 
 * Controls the movement of the parent Entity.
 * 
 * This class is used to control the movement of an Entity object. It can be
 * used to make the entity wander around randomly or move towards a specific
 * destination.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R. S.
 */

public class EntityAI implements Serializable {

    /** The Entity object that this EntityAI controls. */
    protected Entity entity;

    /** The destination x-coordinate for the Entity. */
    private float destX;

    /* The destination y-coordinate for the Entity. */
    private float destY;

    /** Whether the Entity has reached its destination. */
    private boolean reachedDest;

    /** A ScheduledEvent used to make the Entity wander. */
    private ScheduledEvent wandering;

    /**
     * 
     * Constructs a new EntityAI for the specified Entity.
     * 
     * @param e the Entity object that this EntityAI will control
     */
    public EntityAI(Entity e) {
        this.entity = e;
    }

    /**
     * 
     * Makes the Entity wander around randomly.
     * 
     * If the parameter is true, the Entity will start wandering around randomly. If
     * it is false, the Entity will
     * 
     * stop wandering. The Entity will choose a new destination every 5-15 seconds
     * and will continue to do so until
     * 
     * the wander method is called with a false parameter.
     * 
     * @param w whether the Entity should start wandering
     */
    public void wander(boolean w) {
        if (!w && wandering != null) {
            // remove from scheduler
            this.wandering.manualTerminate();
            this.wandering = null;
        }

        // only start once
        if (w && wandering == null) {
            destX = entity.getX() + (float) (Math.random() * 10 - 5);
            destY = entity.getY() + (float) (Math.random() * 10 - 5);

            // 1 second
            final long S = Time.MS_IN_S;

            this.wandering = new ScheduledEvent((long) (Math.random() * (10 * S) + (5 * S)), ScheduledEvent.FOREVER) {
                @Override
                public void run() {
                    this.manualTerminate();

                    destX = entity.getX() + (float) (Math.random() * 10 - 5);
                    destY = entity.getY() + (float) (Math.random() * 10 - 5);

                    this.setDelayMS((long) (Math.random() * (10 * S) + (5 * S)));
                }
            };

            GameScheduler.registerEvent(wandering);
        }
    }

    /**
     * 
     * Moves the Entity towards its destination.
     * This method should be called every game tick to update the Entity's position.
     * The Entity will move towards
     * its destination at a speed determined by its dx and dy values. If the Entity
     * has reached its destination,
     * the reachedDest field will be set to true.
     * 
     * @param e the GameUpdateEvent for the current game tick
     * @see GameUpdateEvent
     */
    public void next(GameUpdateEvent e) {
        float delta_x = entity.getDx() / Time.NS_IN_S * e.getDeltaTimeNS();
        float delta_y = entity.getDy() / Time.NS_IN_S * e.getDeltaTimeNS();

        delta_x *= (destX < entity.getX()) ? -1 : 1;
        delta_y *= (destY < entity.getY()) ? -1 : 1;

        int r = 0;

        if (Math.abs(entity.getX() - destX) >= 0.5f
                && entity.moveTo(e.getMap(), entity.getX() + delta_x, entity.getY()))
            entity.setX(entity.getX() + delta_x);
        else if (Math.abs(entity.getX() - destX) <= 0.5f)
            r++;

        if (Math.abs(entity.getY() - destY) >= 0.5f
                && entity.moveTo(e.getMap(), entity.getX(), entity.getY() + delta_y))
            entity.setY(entity.getY() + delta_y);
        else if (Math.abs(entity.getY() - destY) <= 0.5f)
            r++;

        if (r == 2)
            this.reachedDest = true;
    }

    public void reinit() {

    }

    /**
     * Returns whether the Entity has reached its destination.
     * 
     * @return true if the Entity has reached its destination, false otherwise
     */
    public boolean reachedDest() {
        return this.reachedDest;
    }

    /**
     * Sets the destination for the Entity.
     * 
     * @param destX the x-coordinate of the destination
     * @param destY the y-coordinate of the destination
     */
    public void setDest(float destX, float destY) {
        this.destX = destX;
        this.destY = destY;
    }
}
