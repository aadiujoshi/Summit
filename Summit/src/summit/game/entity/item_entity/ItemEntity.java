package summit.game.entity.item_entity;

import summit.game.GameUpdateEvent;
import summit.game.entity.Entity;

public class ItemEntity extends Entity{

    public ItemEntity(float x, float y) {
        super(x, y, 0.25f, 0.25f);
    }

    @Override
    public void gameClick(GameUpdateEvent e) {
        //do nothing
    }

    @Override
    public void damage(float damage, Entity e) {
        setDestroyed(true);
    }

    @Override
    public void destroy(GameUpdateEvent ge) {
    }
    
    @Override
    public void collide(Entity g) {
        // TODO Auto-generated method stub
        
    }
}
