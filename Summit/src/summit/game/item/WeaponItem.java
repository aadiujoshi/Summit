package summit.game.item;

import summit.game.entity.Entity;
import summit.gfx.ColorFilter;
import summit.gfx.OrderPaintEvent;
import summit.gfx.PaintEvent;
import summit.gfx.Paintable;
import summit.gfx.RenderLayers;
import summit.gfx.Renderer;

public abstract class WeaponItem extends Item implements Paintable{

    private float mult;
    private float baseDamage;

    public WeaponItem(Entity owner) {
        super(owner);

        baseDamage = 1;
        mult = 1;
    }

    @Override
    public void use() {
        getOwner().setAttackDamage(baseDamage * mult);
    }
    
    public void setDamageMult(float mult) {
        this.mult = mult;
    }
    
    public void setBaseDamage(float baseDamage) {
        this.baseDamage = baseDamage;
    }

    @Override
    public void setRenderLayer(OrderPaintEvent e){
        e.addToLayer(RenderLayers.STRUCTURE_ENTITY_LAYER+1, this);
    }

    @Override
    public void paint(PaintEvent e){
        e.getRenderer().renderGame(getSprite(), getOwner().getX()+0.35f, getOwner().getY()-0.2f, 
                                    (getOwner().getRenderOp() & ~Renderer.FLIP_X), 
                                    ColorFilter.NOFILTER, 
                                    e.getCamera());
    }
}
