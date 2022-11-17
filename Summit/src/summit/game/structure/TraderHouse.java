package summit.game.structure;

import summit.gfx.ColorFilter;
import summit.gfx.Sprite;

public class TraderHouse extends Structure{

    public TraderHouse(float x, float y){
        super(x, y, 4, 3);
        super.setSprite(Sprite.VILLAGE_HOUSE);
        super.setColorFilter(new ColorFilter(50, 50, 0));
        super.setShadow(new ColorFilter(-80, -80, -80));
        super.setInnerMap(null);
    }
}