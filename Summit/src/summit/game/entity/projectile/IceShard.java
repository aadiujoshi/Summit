package summit.game.entity.projectile;

import summit.game.GameUpdateEvent;
import summit.game.animation.GlistenAnimation;
import summit.gfx.Sprite;
import summit.util.GameObject;

/**
 * 
 * The IceShard class extends the Projectile class and is used to represent an
 * 
 * ice shard projectile in a game. It adds a glistening effect on the ice shard
 * when it is updating
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R.
 */
public class IceShard extends Projectile {

    /**
     * 
     * Constructs an IceShard object with specified origin, angle, and damage.
     * 
     * @param origin the GameObject from which the ice shard is fired
     * @param angle  the angle at which the ice shard is fired
     * @param damage the amount of damage the ice shard deals when it hits an object
     */
    public IceShard(GameObject origin, float angle, float damage) {
        super(origin, angle, 20, 0.5f, 0.5f);
        super.setAttackDamage(damage);
        super.setSprite(Sprite.ICE_SHARD);
    }

    /**
     * 
     * This method updates the ice shard and adds a glistening effect with a random
     * size and color.
     * 
     * @param e the GameUpdateEvent that caused the update
     */
    @Override
    public void update(GameUpdateEvent e) throws Exception {
        super.update(e);
        if (Math.random() > 0.97)
            e.getMap().addAnimation(new GlistenAnimation(getX(), getY(), (int) (Math.random() * 5 + 1), 0x3792CB));
    }
}
