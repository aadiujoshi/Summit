package summit.game.item;

import summit.game.entity.Entity;
import summit.gfx.Sprite;

public class GreenKey extends Item{

    public GreenKey(Entity owner) {
        super(owner);
        super.setSprite(Sprite.GREEN_KEY);
        super.setTextName("green key");
    }

    @Override
    public Item copy() {
        return new GreenKey(getOwner());
    }
}
