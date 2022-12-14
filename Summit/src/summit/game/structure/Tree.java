/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
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

public class Tree extends Entity{

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

        addItems(new AppleItem(this), (int)(Math.random()*3));
        addItems(new StickItem(this), (int)(Math.random()*3));

        set(pickupItems, false);
        
        set(projectileDamage, false);
    }
    
    @Override
    public void damage(GameUpdateEvent e, Entity hitBy){
        if(this.is(damageCooldown))
            return;
        if(hitBy instanceof Projectile && !is(projectileDamage))
            return;
            
        setHealth(getHealth() - hitBy.getAttackDamage());

        if(getHealth() <= 0){
            hitBy.pickupItems(getItems());
            set(destroyed, true);
        }

        set(damageCooldown, true);
    }

    @Override
    public void paint(PaintEvent e){
        // Point p = Renderer.toPixel(getX(), getY(), e.getCamera());

        // e.getRenderer().fillRect((int)(1+p.x-getWidth()/2*16), (int)(p.y-getHeight()/2*16), (int)(getWidth()*16), (int)(getHeight()*16), Renderer.toIntRGB(255, 0, 0));

        super.paint(e);
    }

    @Override
    public void gameClick(GameUpdateEvent e) {
        if(this.is(damageCooldown))
            return;

        damage(e, e.getMap().getPlayer());
        e.getMap().addAnimation(new ParticleAnimation(getX(), getY()-0.25f, 
                                        500, 20, getColor()));
    }
}
