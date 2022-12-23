package summit.game.item;

import summit.game.entity.Entity;
import summit.gfx.Sprite;

public class BlueKey extends Item{

    public BlueKey(Entity owner) {
        super(owner);
        super.setSprite(Sprite.BLUE_KEY);
        super.setTextName("blue key");
    }

    @Override
    public Item copy() {
        return new BlueKey(getOwner());
    }
}
