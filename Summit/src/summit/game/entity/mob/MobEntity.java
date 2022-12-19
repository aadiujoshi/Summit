/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.entity.mob;

import summit.game.GameUpdateEvent;
import summit.game.ai.EntityAI;
import summit.game.entity.Entity;
import summit.game.entity.projectile.Projectile;
import summit.gfx.ColorFilter;
import summit.gfx.Light;
import summit.gfx.PaintEvent;
import summit.gfx.Renderer;
import summit.util.Direction;

public abstract class MobEntity extends Entity{

    private EntityAI ai;
    // private ItemTable items;
    
    public MobEntity(float x, float y, float width, float height) {
        super(x, y, width, height);
        super.setFacing(Direction.SOUTH);
        super.setDx(2);
        super.setDy(2);
        super.setColor(Renderer.toIntRGB(200, 0, 0));

        super.setAttackCooldownMS(500);
        super.setDamageCooldownMS(500);

        add(pickupItems);
        add(hostile);
    }

    @Override
    public void damage(Entity hitBy){
        super.damage(hitBy);
        if(getHealth() <= 0){
            Player pl = null;

            if((hitBy instanceof Projectile && ((Projectile)hitBy).getOrigin() instanceof Player)){
                pl = (Player)((Projectile)hitBy).getOrigin();
            } else if(hitBy instanceof Player){
                pl = (Player)hitBy;
            }
            
            if(pl != null){
                if(getCurMap().equals("DungeonsMap") && is(MobEntity.hostile)){
                    if(Math.random() < 1){
                        pl.getObtainedKeys()[0] = true;
                    }
                }

                pl.addXp(3);
            }
        }
    }

    @Override
    public void reinit(){
        super.reinit();
        ai.reinit();
    }

    @Override
    public void update(GameUpdateEvent e){
        super.update(e);

        if(ai != null)
            ai.next(e);
    }

    @Override
    public void paint(PaintEvent e){
        // if(this instanceof Player)
        // System.out.println(getLight() != Light.NO_LIGHT);

        if(getCurMap().equals("DungeonsMap") && getLight() == Light.NO_LIGHT)
            setColorFilter(new ColorFilter(-100, -100, -100));
            
        super.paint(e);
    }
    
    @Override
    public void gameClick(GameUpdateEvent e) {
    }

    @Override
    public void collide(Entity contact) {
        super.collide(contact);

        
        // if(contact instanceof Item){
        //     Item c = (Item)contact;
        //     if(is(pickupItems) && items != null){
        //         getItems().addItem(c);
        //     }
        // }
    }

    //------  getters and setters -------------------------------------------
    
    public EntityAI getAI() {
        return this.ai;
    }

    public void setAI(EntityAI ai) {
        this.ai = ai;
    }
    
    // public ItemTable getItems() {
    //     return this.items;
    // }

    // public void setItems(ItemTable items) {
    //     this.items = items;
    // }

    //-----------  game tag / property keys ------------------------------

    public static String pickupItems = "pickupItems";
    public static String hostile = "hostile";

    //-------------------------------------------------------------------

}
