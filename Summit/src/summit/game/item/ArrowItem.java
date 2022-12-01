package summit.game.item;

import summit.gfx.Sprite;
import summit.util.Region;

public class ArrowItem extends Item{
    public ArrowItem(float x, float y) {
        super(new Region(x, y, 0.25f, 0.25f), new Region(0, 0, 0.5f, 0.5f));
        super.setSprite(Sprite.ARROW_PROJ);
    }
}
