/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.deprecated.item.weapon;

import summit.deprecated.item.Item;
import summit.gfx.Sprite;
import summit.util.Region;

public class SwordItem extends Item {

    public SwordItem(float x, float y, int state) {
        super(((state == Item.GAME) ?
                new Region(x, y, 1, 1) :
                new Region(x, y, y, state)), 
                state);
        super.setSprite(Sprite.STONE_SWORD);
    }

    @Override
    public Item switchState() {
        return null;
    }
}
