package summit.game.item;

import summit.game.GameUpdateEvent;
import summit.game.entity.Entity;
import summit.gfx.Sprite;

public class AppleItem extends Item {

    public AppleItem(Entity owner) {
        super(owner);
        super.setSprite(Sprite.APPLE_ITEM);
        super.setTextName("apples");
    }

    @Override
    public void use() {
        if(getOwner().getHealth() == getOwner().getMaxHealth())
            return;
            
        getOwner().modHealth(0.5f);
        setUsed(true);
    }
    
    @Override
    public Item copy(){
        return new AppleItem(getOwner());
    }
}
