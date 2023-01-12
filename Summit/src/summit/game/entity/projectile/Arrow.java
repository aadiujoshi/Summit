/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.entity.projectile;

import summit.gfx.Sprite;
import summit.util.GameObject;

/**
 * 
 * The Arrow class extends the Projectile class and is used to represent an
 * arrow projectile in a game.
 * 
 * @author @author Aadi J, Aditya B, Sanjay R, Aadithya R.
 */
public class Arrow extends Projectile {

    /**
     * 
     * Constructs an Arrow object with specified origin, angle, and damage.
     * 
     * @param origin the GameObject from which the arrow is fired
     * @param angle  the angle at which the arrow is fired
     * @param damage the amount of damage the arrow deals when it hits an object
     */
    public Arrow(GameObject origin, float angle, float damage) {
        super(origin, angle, 15, 0.5f, 0.25f);
        super.setColor(0xDCDCDC);
        super.setSprite(Sprite.ARROW_PROJ);
        super.setAttackDamage(damage);
    }
}
