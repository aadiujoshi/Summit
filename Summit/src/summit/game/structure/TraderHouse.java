package summit.game.structure;

import summit.game.structure;

public class TraderHouse extends Structure{

    public TraderHouse(float x, float y){
        super(x, y, 4, 3);
        super.setSprite(Sprite.VILLAGE_HOUSE);
        super.setInnerMap(null);
    }
}