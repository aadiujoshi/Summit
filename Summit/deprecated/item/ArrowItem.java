/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.deprecated.item;

import summit.gfx.Sprite;
import summit.util.Region;

public class ArrowItem extends Item{
    public ArrowItem(float x, float y) {
        super(new Region(x, y, 1, 1), 1);
        
        super.setSprite(Sprite.ARROW_PROJ);
    }

    @Override
    public Item switchState() {
        // TODO Auto-generated method stub
        return null;
    }
}
