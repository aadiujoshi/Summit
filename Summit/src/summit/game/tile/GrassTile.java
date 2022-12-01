/*
* BPA project by Aadi Joshi, Aditya Bhattaharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.tile;

import summit.game.GameUpdateEvent;
import summit.gfx.Sprite;

public class GrassTile extends Tile{

    public GrassTile(float x, float y) {
        super(x, y);
        super.setSprite(Sprite.GRASS_TILE);
        
    }

    @Override
    public void update(GameUpdateEvent e) {
        
    }
}
