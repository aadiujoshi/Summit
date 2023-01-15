package summit.game.entity.mob;

import summit.game.GameUpdateEvent;
import summit.game.ai.HostileMobAI;
import summit.game.entity.Entity;
import summit.game.item.GoldCoin;
import summit.game.item.Sword;
import summit.gfx.ColorFilter;
import summit.gfx.PaintEvent;
import summit.gfx.Renderer;
import summit.gfx.Sprite;

/**
 * 
 * The Zombie class represents a type of hostile mob in the game.
 * It extends the HumanoidEntity class and has a HostileMobAI instance as its
 * AI.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R. S.
 */
public class Zombie extends HumanoidEntity {

    /**
     * Constructs a new Zombie instance at the given x and y position.
     * 
     * The zombie has 5 health points, a HostileMobAI instance as its AI, and is
     * equipped with a Sword.
     * It also carries a random number of GoldCoin items.
     * 
     * Its sprite states are: submerged facing south, facing backwards, and neutral
     * facing south.
     * 
     * The zombie is marked as hostile.
     * 
     * @param x the x position of the zombie
     * @param y the y position of the zombie
     */
    public Zombie(float x, float y) {
        super(x, y);
        super.setAI(new HostileMobAI(this));
        super.setHealth(5);
        super.setEquipped(new Sword(this));

        super.setSpriteStates(Sprite.PLAYER_SUBMERGED_SOUTH,
                Sprite.PLAYER_FACE_BACK_1,
                Sprite.PLAYER_NEUTRAL_SOUTH);

        super.setMaxHealth(5);

        super.addItems(new GoldCoin(this), (int) (Math.random() * 10));

        super.set(hostile, true);
    }

    /**
     * Overrides the paint method of the HumanoidEntity class to set a green color
     * filter on the sprite
     * if the zombie is not in a damage cooldown state.
     * 
     * @param e the PaintEvent object containing information about the rendering
     *          context
     */
    @Override
    public void paint(PaintEvent e) {
        if (!is(damageCooldown)) {
            setColorFilter(new ColorFilter(0, 100, 0));
        }

        super.paint(e);
    }
}
