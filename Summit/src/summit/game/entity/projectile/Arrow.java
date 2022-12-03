/*
* BPA project by Aadi Joshi, Aditya Bhattaharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.entity.projectile;

import summit.util.GameRegion;

public class Arrow extends Projectile {

    public Arrow(GameRegion origin, float angle) {
        super(origin, angle, 12, 0.5f, 0.25f);
    }
}
