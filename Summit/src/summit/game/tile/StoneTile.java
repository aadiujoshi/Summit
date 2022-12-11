/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.tile;

import summit.game.GameUpdateEvent;
import summit.gfx.Renderer;
import summit.gfx.Sprite;

public class StoneTile extends Tile{

    public StoneTile(float x, float y) {
        super(x, y);
        super.setSprite(Sprite.STONE_TILE);
        super.particleAnimation(true);
        super.setColor(Renderer.toIntRGB(100, 100, 100));
    }
}
