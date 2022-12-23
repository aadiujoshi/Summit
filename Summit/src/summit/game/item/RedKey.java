package summit.game.item;

import summit.game.entity.Entity;
import summit.gfx.Sprite;

public class RedKey extends Item{

    public RedKey(Entity owner) {
        super(owner);
        super.setSprite(Sprite.RED_KEY);
        super.setTextName("red key");
    }

    @Override
    public Item copy() {
        return new RedKey(getOwner());
    }
}
