/*
* BPA project by Aadi Joshi, Aditya Bhattaharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.item;

import summit.gfx.Sprite;
import summit.util.Region;

public class SnowballItem extends Item{

    public SnowballItem(float x, float y) {
        super(new Region(x, y, 0.5f, 0.5f), new Region(0, 0, x, y));
        // super.setStackable(true);
        super.setSprite(Sprite.SNOWBALL);
    }

    public SnowballItem(){
        this(0, 0);
    }
}
