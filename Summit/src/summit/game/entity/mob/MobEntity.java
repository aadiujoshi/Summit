/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.entity.mob;

import java.util.HashMap;

import summit.game.GameUpdateEvent;
import summit.game.ai.EntityAI;
import summit.game.entity.Entity;
import summit.game.entity.projectile.Projectile;
import summit.game.item.BlueKey;
import summit.game.item.GreenKey;
import summit.game.item.RedKey;
import summit.game.item.WeaponItem;
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

        addItems(new RedKey(this), 0);
        addItems(new GreenKey(this), 0);
        addItems(new BlueKey(this), 0);

        add(pickupItems);

        set(pickupItems, true);
        
        add(hostile);
    }

    @Override
    public void damage(GameUpdateEvent e, Entity hitBy){
        super.damage(e, hitBy);
        if(getHealth() <= 0){

            if(hitBy instanceof Projectile)
                hitBy = (Entity)((Projectile)hitBy).getOrigin();
            
            //if player hasnt gotten the red key yet
            if(e.getMap().getPlayer().getObtainedKeys()[0] == false){

                if(getCurMap().equals("DungeonsMap") && is(MobEntity.hostile)){

                    double chance = Math.random();
                    System.out.println(chance);

                    //10 percent chance to get key
                    if(chance < 0.1){
                        addItems(new RedKey(this), 1);
                    }
                }
            }
            
            if(hitBy.is(pickupItems)){
                hitBy.pickupItems(this.getItems());
            }
        }
    }

    @Override
    public void reinit(){
        super.reinit();
        if(ai != null)
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

        ColorFilter cf = getColorFilter();

        if(getCurMap().equals("DungeonsMap") && getLight() == Light.NO_LIGHT)
            setColorFilter(new ColorFilter(
                            cf.getRed()-100, 
                            cf.getGreen()-100, 
                            cf.getBlue()-100));
            
        super.paint(e);
        setColorFilter(cf);
    }
    
    @Override
    public void gameClick(GameUpdateEvent e) {
    }

    // @Override
    // public void collide(GameUpdateEvent ev, Entity contact) {
    //     super.collide(ev, contact);

        
    //     // if(contact instanceof Item){
    //     //     Item c = (Item)contact;
    //     //     if(is(pickupItems) && items != null){
    //     //         getItems().addItem(c);
    //     //     }
    //     // }
    // }

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

    public static String hostile = "hostile";

    //-------------------------------------------------------------------

}
