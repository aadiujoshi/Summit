package summit.game.item;

import summit.game.entity.Entity;
import summit.game.entity.mob.MobEntity;
import summit.gfx.Sprite;

/**
 *
 * 
 * The GoldCoin class represents a gold coin in the game. It is a subclass of
 * the
 * 
 * {@link Item} class and represents a coin that can be used as currency.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R.
 */
public class GoldCoin extends Item {

    /**
     *
     * 
     * Creates a new instance of the GoldCoin class and sets the owner of the coin
     * to the specified Entity.
     * 
     * @param owner The Entities that owns this coin
     */
    public GoldCoin(Entity owner) {
        super(owner);
        super.setSprite(Sprite.GOLD_COIN);
        super.setTextName("gold");
    }

    /**
     *
     * 
     * Creates a copy of the current gold coin.
     * 
     * @return A copy of the current gold coin.
     */
    @Override
    public Item copy() {
        return new GoldCoin(getOwner());
    }
}
