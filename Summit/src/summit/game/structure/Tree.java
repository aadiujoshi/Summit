package summit.game.structure;

import java.util.Stack;

import summit.game.GameUpdateEvent;
import summit.game.animation.ParticleAnimation;
import summit.game.entity.Entity;
import summit.game.entity.mob.Player;
import summit.game.entity.projectile.Projectile;
import summit.game.item.AppleItem;
import summit.game.item.Item;
import summit.game.item.StickItem;
import summit.gfx.Light;
import summit.gfx.PaintEvent;
import summit.gfx.Sprite;

/**
 * 
 * Tree class is a class that represents tree in the game. It is a subclass of
 * Entities class.
 * 
 * It creates a tree object with its position, health, sprite, shadow and items.
 * 
 * It also handles the damage, paint and gameClick events for the tree.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R.
 */
public class Tree extends Entity {

    /**
     * 
     * Constructs a Tree object with the given position.
     * 
     * @param x the x position of the tree
     * 
     * @param y the y position of the tree
     */
    public Tree(float x, float y) {
        super(x, y, 1, 1);

        super.setMaxHealth(5);
        super.setMoveable(false);
        super.setHealth(5);
        super.setSprite(Sprite.PINE_TREE);
        super.setShadow(new Light(x, y, 1, -150, -150, -150));
        super.setSpriteOffsetY(1.5f);
        super.setDamageCooldownMS(300);
        super.setColor(0x964B00);

        set(pickupItems, true);

        addItems(new AppleItem(this), (int) (Math.random() * 3));
        addItems(new StickItem(this), (int) (Math.random() * 3));

        set(pickupItems, false);

        set(projectileDamage, false);
    }

    /**
     * 
     * Damages the tree object by the given Entities object. It also handles the
     * logic for when the tree is destroyed.
     * 
     * @param e     the game update event
     * 
     * @param hitBy the Entities object that damages the tree
     */
    @Override
    public void damage(GameUpdateEvent e, Entity hitBy) {
        if (this.is(damageCooldown))
            return;
        if (hitBy instanceof Projectile && !is(projectileDamage))
            return;

        setHealth(getHealth() - hitBy.getAttackDamage());

        if (getHealth() <= 0) {
            hitBy.pickupItems(getItems());
            set(destroyed, true);
        }

        set(damageCooldown, true);
    }

    /**
     * 
     * Paints the tree object on the screen.
     * 
     * @param e the paint event
     */
    @Override
    public void paint(PaintEvent e) {
        // Point p = Renderer.toPixel(getX(), getY(), e.getCamera());

        // e.getRenderer().fillRect((int)(1+p.x-getWidth()/2*16),
        // (int)(p.y-getHeight()/2*16), (int)(getWidth()*16), (int)(getHeight()*16),
        // Renderer.toIntRGB(255, 0, 0));

        super.paint(e);
    }

    /**
     * 
     * Handles the game click event for the tree object.
     * 
     * @param e the game update event
     */
    @Override
    public void gameClick(GameUpdateEvent e) {
        if (this.is(damageCooldown))
            return;

        damage(e, e.getMap().getPlayer());
        e.getMap().addAnimation(new ParticleAnimation(getX(), getY() - 0.25f,
                500, 20, getColor()));
    }
}
