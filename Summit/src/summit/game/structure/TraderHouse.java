/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.structure;

import summit.game.GameMap;
import summit.game.mapgenerator.GameMapGenerator;
import summit.gfx.ColorFilter;
import summit.gfx.Sprite;

public class TraderHouse extends Building{
    
    public TraderHouse(float x, float y, GameMap parentMap){
        super(x, y, 4, 3, GameMapGenerator.generateTraderHouse(parentMap), parentMap);
        super.setSprite(Sprite.VILLAGE_HOUSE);
        // super.setColorFilter(new ColorFilter(50, 50, 0));
        super.setSpriteOffsetY(0.5f);
        // super.setShadow(new ColorFilter(-100, -100, -100));
        super.setShadow(ColorFilter.NOFILTER);
    }
}