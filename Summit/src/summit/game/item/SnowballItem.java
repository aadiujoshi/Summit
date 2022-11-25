package summit.game.item;

import summit.game.GameUpdateEvent;
import summit.game.entity.Entity;

public class SnowballItem extends Item{

    public SnowballItem(float x, float y) {
        super(x, y, 0.5f, 0.5f);
        super.setStackable(true);
    }
}
