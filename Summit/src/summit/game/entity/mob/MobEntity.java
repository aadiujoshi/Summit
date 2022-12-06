/*
* BPA project by Aadi Joshi, Aditya Bhattaharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.entity.mob;

import summit.game.GameUpdateEvent;
import summit.game.ai.EntityAI;
import summit.game.animation.ParticleAnimation;
import summit.game.entity.Entity;
import summit.game.item.Item;
import summit.game.item.itemtable.ItemTable;
import summit.gfx.ColorFilter;
import summit.gfx.PaintEvent;
import summit.gfx.Renderer;
import summit.gfx.Sprite;
import summit.util.Direction;
import summit.util.GameRegion;

public abstract class MobEntity extends Entity{

    private EntityAI ai;
    private ItemTable items;
    
    public MobEntity(float x, float y, float width, float height) {
        super(x, y, width, height);
        super.setFacing(Direction.SOUTH);
        super.setDx(3);
        super.setDy(3);
        super.setColor(Renderer.toIntRGB(200, 0, 0));

        add(pickupItems);
    }

    @Override
    public void update(GameUpdateEvent e){
        super.update(e);

        if(ai != null)
            ai.next(e);
    }

    @Override
    public void paint(PaintEvent e){
        super.paint(e);
        
        if(getFacing() == Direction.SOUTH)
            e.getRenderer().renderGame(destroyed, getX(), getY()-getHeight()/2, Renderer.NO_OP, ColorFilter.NOFILTER, e.getCamera());
    }

    @Override
    public void collide(Entity contact) {
        super.collide(contact);

        if(contact instanceof Item){
            Item c = (Item)contact;
            if(is(pickupItems)){
                c.setStashed(true);
                getItems().addItem(c);
            }
        }
    }

    //------  getters and setters -------------------------------------------
    
    public EntityAI getAI() {
        return this.ai;
    }

    public void setAI(EntityAI ai) {
        this.ai = ai;
    }
    
    public ItemTable getItems() {
        return this.items;
    }

    public void setItems(ItemTable items) {
        this.items = items;
    }

    
    //-----------  game tag / property keys ------------------------------

    public static String pickupItems = "pickupItems";

    //-------------------------------------------------------------------

}
