/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.entity.projectile;

import summit.game.GameUpdateEvent;
import summit.game.entity.Entity;
import summit.gfx.PaintEvent;
import summit.gfx.Renderer;
import summit.util.GameObject;
import summit.util.Region;
import summit.util.Time;

/**
 * 
 * The Projectile class is an extension of the Entity class and is used to
 * represent a projectile in a game.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R.
 */
public class Projectile extends Entity {

    // starting positions
    private float sx;
    private float sy;

    private float contactDamage;

    private GameObject origin;

    /**
     * 
     * Constructs a Projectile object with specified origin, angle, speed, width,
     * and height.
     * 
     * @param origin the GameObject from which the projectile is fired
     * 
     * @param angle  the angle at which the projectile is fired
     * 
     * @param speed  the speed at which the projectile moves
     * 
     * @param width  the width of the projectile
     * 
     * @param height the height of the projectile
     */
    public Projectile(GameObject origin, float angle, float speed, float width, float height) {
        super(origin.getX(), origin.getY(), width, height);

        super.setDx(speed * (float) Math.cos(angle));
        super.setDy(speed * (float) Math.sin(angle));

        // super.setLight(new Light(getX(), getY(), 0.25f, -100, -100, -100));

        super.setHealth(1);

        this.origin = origin;

        this.sx = getX();
        this.sy = getY();

        float deg = (float) Math.toDegrees(angle);

        if (deg < 0) {
            deg = 360 + deg;
        }

        // face east
        if ((deg <= 45 && deg >= 0) || (deg <= 360 && deg >= 315)) {
            setRenderOp(Renderer.FLIP_X);
        }
        // face west
        else if (deg <= 215 && deg >= 135) {
            setRenderOp(Renderer.NO_OP);
        }
        // face north
        else if (deg < 135 && deg > 45) {
            setRenderOp(Renderer.ROTATE_90);
        }
        // face south
        else if (deg < 315 && deg > 215) {
            setRenderOp(Renderer.ROTATE_90 | Renderer.FLIP_Y);
        }
    }

    /**
     * 
     * This method is called when the projectile is attacking a target.
     * 
     * @param targetX the x-coordinate of the target
     * @param targetY the y-coordinate of the target
     * @param ev      the GameUpdateEvent that caused the attack
     */
    @Override
    public void attack(float targetX, float targetY, GameUpdateEvent ev) {
    }

    /**
     * 
     * This method is called when the projectile is being painted.
     * 
     * @param e the PaintEvent that caused the painting
     */
    @Override
    public void paint(PaintEvent e) {

        // System.out.println(getFacing());

        // setRenderOp(
        // switch(getFacing()){
        // case EAST -> Renderer.FLIP_X;
        // case NORTH -> Renderer.ROTATE_90;
        // case SOUTH -> Renderer.ROTATE_90 | Renderer.FLIP_Y;
        // case WEST -> Renderer.NO_OP;
        // case NW -> 0;
        // case NE -> 0;
        // case SW -> 0;
        // case SE -> 0;
        // });

        super.paint(e);
    }

    /**
     * 
     * This method updates the projectile.
     * 
     * @param e the GameUpdateEvent that caused the update
     */
    @Override
    public void update(GameUpdateEvent e) throws Exception {
        if (Region.distance(getX(), getY(), sx, sy) >= 20) {
            set(destroyed, true);
            return;
        }

        float delta_t = (float) e.getDeltaTimeNS() / Time.NS_IN_S;

        float nx = getX() + getDx() * delta_t;
        float ny = getY() + getDy() * delta_t;

        if (moveTo(e.getMap(), nx, ny)) {
            setPos(nx, ny);
        } else {
            set(destroyed, true);
            return;
        }

        set(inWater, false);
    }

    /**
     * This method is called when the projectile collides with another Entity.
     *
     * @param e       the GameUpdateEvent that caused the collision
     * @param contact the Entities that the projectile collided with
     */
    @Override
    public void collide(GameUpdateEvent e, Entity contact) {
        if (((GameObject) contact) == origin)
            return;

        contact.damage(e, this);

        set(destroyed, true);
    }

    /**
     * This method returns the dx of the projectile with modified speed if it is in
     * water.
     * 
     * @return the dx of the projectile
     */
    @Override
    public float getDx() {
        // change speed back to normal
        return super.getDx() * (is(inWater) ? 2 : 1);
    }

    /**
     * This method returns the dy of the projectile with modified speed if it is in
     * water.
     * 
     * @return the dy of the projectile
     */
    @Override
    public float getDy() {
        // change speed back to normal
        return super.getDy() * (is(inWater) ? 2 : 1);
    }

    
    /** 
     * @param e
     */
    @Override
    public void gameClick(GameUpdateEvent e) {

    }

    
    /** 
     * @return GameObject
     */
    public GameObject getOrigin() {
        return this.origin;
    }

    /**
     * This method returns the contact damage of the projectile.
     * 
     * @return the contact damage of the projectile
     */
    @Override
    public float getAttackDamage() {
        return this.contactDamage;
    }

    /**
     * This method sets the contactDamage of the projectile.
     * 
     * @param contactDamage the contactDamage of the projectile
     */
    public void setAttackDamage(float contactDamage) {
        this.contactDamage = contactDamage;
    }
}
