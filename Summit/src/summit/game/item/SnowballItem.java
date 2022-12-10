/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.item;

import summit.gfx.Sprite;
import summit.util.Region;

public class SnowballItem extends Item{

    public SnowballItem(float x, float y) {
        super(new Region(x, y, 1, 1), 1);
        
        // super.setStackable(true);
        super.setSprite(Sprite.SNOWBALL);
    }

    public SnowballItem(){
        this(0, 0);
    }

    @Override
    public Item switchState() {
        // TODO Auto-generated method stub
        return null;
    }
}
