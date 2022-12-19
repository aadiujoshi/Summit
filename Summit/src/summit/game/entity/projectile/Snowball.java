/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.entity.projectile;

import summit.gfx.Sprite;
import summit.util.GameRegion;

public class Snowball extends Projectile {

    public Snowball(GameRegion origin, float angle) {
        super(origin, angle, 20, 0.5f, 0.5f);
        super.setSprite(Sprite.SNOWBALL);
        super.setAttackDamage(0);
    }
}
