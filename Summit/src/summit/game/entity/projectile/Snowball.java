/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.entity.projectile;

import summit.gfx.Sprite;
import summit.util.GameObject;

/**
 * 
 * The Snowball class extends the Projectile class and is used to represent a
 * 
 * snowball projectile in a game.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R.
 */
public class Snowball extends Projectile {

    /**
     * 
     * Constructs a Snowball object with specified origin, angle, and damage.
     * 
     * @param origin the GameObject from which the snowball is thrown
     * @param angle  the angle at which the snowball is thrown
     * @param damage the amount of damage the snowball deals when it hits an object
     */
    public Snowball(GameObject origin, float angle, float damage) {
        super(origin, angle, 20, 0.5f, 0.5f);
        super.setSprite(Sprite.SNOWBALL);
        super.setAttackDamage(damage);
    }
}
