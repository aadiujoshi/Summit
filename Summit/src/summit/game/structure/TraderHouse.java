package summit.game.structure;

import summit.game.mapgenerator.GameMapGenerator;
import summit.gfx.ColorFilter;
import summit.gfx.Sprite;

public class TraderHouse extends Structure{
    public TraderHouse(float x, float y){
        super(x, y, 4, 3, GameMapGenerator.generateTraderHouse());
        super.setSprite(Sprite.VILLAGE_HOUSE);
        // super.setColorFilter(new ColorFilter(50, 50, 0));
        super.setSpriteOffsetY(0.5f);
        super.setShadow(new ColorFilter(-100, -100, -100));
        super.setInnerMap(null);
    }
}