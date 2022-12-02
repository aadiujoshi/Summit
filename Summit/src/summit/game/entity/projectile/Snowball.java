/*
* BPA project by Aadi Joshi, Aditya Bhattaharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.entity.projectile;

import summit.gfx.Sprite;

public class Snowball extends Projectile {

    public Snowball(float x, float y, float angle) {
        super(x, y, angle, 10, 0.5f, 0.5f);
        super.setSprite(Sprite.SNOWBALL);
    }
}
