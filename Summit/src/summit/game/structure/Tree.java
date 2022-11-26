package summit.game.structure;

import summit.game.GameMap;
import summit.game.GameUpdateEvent;
import summit.game.entity.Entity;
import summit.gfx.Sprite;

public class Tree extends Entity{

    public Tree(float x, float y) {
        super(x, y, 1, 1);
        super.setMaxHealth(5);
        super.setHealth(5);
        super.setSprite(Sprite.PINE_TREE);
        super.setSpriteOffsetY(0.5f);
    }

    @Override
    public void gameClick(GameUpdateEvent e) {
        
        damage(1, e.getMap().getPlayer());
    }

    @Override
    public void destroy(GameUpdateEvent ge) {
        
    }

    @Override
    public void collide(Entity g) {
        
    }
}
