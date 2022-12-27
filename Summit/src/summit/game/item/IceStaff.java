package summit.game.item;

import summit.game.entity.Entity;
import summit.game.entity.projectile.IceShard;
import summit.gfx.ColorFilter;
import summit.gfx.Light;
import summit.gfx.PaintEvent;
import summit.gfx.Sprite;

public class IceStaff extends ProjectileWeapon{

    public IceStaff(Entity owner) {
        super(owner);
        super.setSprite(Sprite.ICE_STAFF);
        super.setBaseDamage(3.5f);
        super.setTextName("ice staff");
        super.setProjType(IceShard.class);
    }
    
    @Override
    public void reinit(){
        super.setProjType(IceShard.class);
    }

    @Override
    public void use() {
        //do nothing
    }

    @Override
    public Item copy() {
        return new IceStaff(getOwner());
    }

    @Override
    public void paint(PaintEvent e){
        super.paint(e);
        if(!getOwner().is(Entity.inWater))
            e.getRenderer().renderLight(new Light(
                    super.getX()+0.9f, 
                    super.getY()+0.5f, 
                    1, new ColorFilter(0, 40, 120)), e.getCamera());
    }
}
