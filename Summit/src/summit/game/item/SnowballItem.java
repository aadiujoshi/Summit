package summit.game.item;

import summit.gfx.Sprite;

public class SnowballItem extends Item{

    public SnowballItem(float x, float y) {
        super(x, y, 0.5f, 0.5f);
        super.setStackable(true);
        super.setSprite(Sprite.SNOWBALL);
    }
}
