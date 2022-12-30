/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.entity.projectile;

import summit.gfx.Sprite;
import summit.util.GameObject;

public class Snowball extends Projectile {

    public Snowball(GameObject origin, float angle, float damage) {
        super(origin, angle, 20, 0.5f, 0.5f);
        super.setSprite(Sprite.SNOWBALL);
        super.setAttackDamage(damage);
    }
}
