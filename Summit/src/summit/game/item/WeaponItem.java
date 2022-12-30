package summit.game.item;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import summit.game.GameUpdateEvent;
import summit.game.entity.Entity;
import summit.game.entity.projectile.Projectile;
import summit.gfx.Camera;
import summit.gfx.ColorFilter;
import summit.gfx.OrderPaintEvent;
import summit.gfx.PaintEvent;
import summit.gfx.Paintable;
import summit.gfx.RenderLayers;
import summit.gfx.Renderer;
import summit.util.GameObject;
import summit.util.Region;

public abstract class WeaponItem extends Item implements Paintable{

    private float attackRange;

    private float mult;
    private float baseDamage;

    private float x;
    private float y;

    public WeaponItem(Entity owner) {
        super(owner);

        // ATTACK_TYPE = type;
        baseDamage = 1;
        mult = 1;
    }

    @Override
    public abstract void use();
    
    public void addLevel() {
        this.mult += 0.2f;
    }
    
    public float getNetDamage() {
        return this.baseDamage*mult;
    }

    public void setBaseDamage(float baseDamage) {
        this.baseDamage = baseDamage;
    }
    
    public abstract boolean useWeapon(float targetX, float targetY, GameUpdateEvent e);

    @Override
    public void setRenderLayer(OrderPaintEvent e){
        e.addToLayer(RenderLayers.STRUCTURE_ENTITY_LAYER, this);
    }

    @Override
    public void paint(PaintEvent e){
        Entity owner = getOwner();

        if(owner.is(Entity.inWater))
            return;

        x = owner.getX();
        y = owner.getY();

        //prevent stuttering
        if(owner.getName().equals("Player")){
            x = e.getCamera().getX();
            y = e.getCamera().getY();
        }
        
        this.x += owner.getSpriteOffsetX();
        this.y += owner.getSpriteOffsetY();

        e.getRenderer().renderGame(getSprite(), x+0.5f, y-0.2f, 
                                    (getOwner().getRenderOp() & ~Renderer.FLIP_X), 
                                    ColorFilter.NOFILTER, 
                                    e.getCamera());
    }
    
    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getAttackRange() {
        return this.attackRange;
    }

    public void setAttackRange(float attackRange) {
        this.attackRange = attackRange;
    }
}
