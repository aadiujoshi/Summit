/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.entity.projectile;

import summit.gfx.Sprite;
import summit.util.GameObject;

public class Arrow extends Projectile {

    public Arrow(GameObject origin, float angle, float damage) {
        super(origin, angle, 15, 0.5f, 0.25f);
        super.setColor(0xDCDCDC);
        super.setSprite(Sprite.ARROW_PROJ);
        super.setAttackDamage(damage);
    }
}
