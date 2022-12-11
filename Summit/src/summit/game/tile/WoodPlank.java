/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.tile;

import summit.gfx.Sprite;

public class WoodPlank extends Tile{

    public WoodPlank(float x, float y) {
        super(x, y);
        super.setSprite(Sprite.WOOD_PLANK);
    }
}
