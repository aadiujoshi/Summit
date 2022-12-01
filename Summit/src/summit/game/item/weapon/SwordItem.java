package summit.game.item.weapon;

import summit.game.item.Item;
import summit.gfx.Sprite;
import summit.util.Region;

public class SwordItem extends Item {

    public SwordItem(float x, float y) {
        super(new Region(x, y, 1, 1), new Region(0, 0, 1, 2));
        super.setSprite(Sprite.STONE_SWORD);
    }

    public SwordItem() {
        this(0, 0);
    }    
}
