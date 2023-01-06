package summit.game.entity.mob;

import summit.game.GameUpdateEvent;
import summit.game.ai.HostileMobAI;
import summit.game.entity.Entity;
import summit.game.item.ArrowItem;
import summit.game.item.BoneItem;
import summit.game.item.Bow;
import summit.game.item.GoldCoin;
import summit.game.item.Sword;
import summit.gfx.Sprite;

/**
 * 
 * The Skeleton class represents a hostile mob in the game. It extends the
 * HumanoidEntity class
 * and has a HostileMobAI for its AI. The Skeleton has a Bow as its equipped
 * weapon and drops
 * Arrows and GoldCoins when it is destroyed. It also has a random amount of
 * Bones in its inventory.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R.
 */
public class Skeleton extends HumanoidEntity {

    /**
     * Constructs a new Skeleton at the specified x and y position.
     * 
     * @param x the x position of the Skeleton
     * @param y the y position of the Skeleton
     */
    public Skeleton(float x, float y) {
        super(x, y);
        super.setSprite(Sprite.SKELETON_FACE_BACK);
        super.setAI(new HostileMobAI(this));
        super.setAttackCooldownMS(1000);
        super.setMaxHealth(7);
        super.setHealth(getMaxHealth());

        super.setSpriteStates(Sprite.SKELETON_SUBMERGED,
                Sprite.SKELETON_FACE_BACK,
                Sprite.SKELETON_NEUTRAL);

        super.setEquipped(new Bow(this));

        super.addItems(new BoneItem(this), (int) (Math.random() * 7));
        super.addItems(new GoldCoin(this), (int) (Math.random() * 7));

        // drop these items once destroyed
        super.addItems(new ArrowItem(this), 100);

        super.set(hostile, true);
    }

    /**
     * 
     * Called when the Skeleton is damaged by another Entity. If the Skeleton's
     * health falls to or below
     * 
     * 0 after taking damage, it will drop a random amount of Arrows and all of its
     * GoldCoins.
     * 
     * @param e     the GameUpdateEvent
     * 
     * @param hitBy the Entity that damaged the Skeleton
     */
    @Override
    public void damage(GameUpdateEvent e, Entity hitBy) {
        if (getHealth() - hitBy.getAttackDamage() <= 0) {
            // clear arrows
            getItems().remove(Sprite.ARROW_ITEM);

            // add random amount of arrow
            addItems(new ArrowItem(this), (int) (Math.random() * 5));
        }
        super.damage(e, hitBy);
    }
}
