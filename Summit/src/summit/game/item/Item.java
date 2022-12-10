/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.item;

import summit.game.GameUpdateEvent;
import summit.game.entity.Entity;
import summit.gfx.PaintEvent;
import summit.util.GameRegion;
import summit.util.Region;

public abstract class Item extends Entity{

    public static int GAME = 1;
    public static int TABLE = 2;

    //picked up by the mouse
    private boolean selected = false;

    //can stack on other of the same items
    private boolean stackable = true;

    //if in inventory or in game
    //TABLE -> in an inventory or lootTable
    //GAME -> on the floor in the game
    private final int STATE;
    
    //sprite thats rendered in the inventory/gui
    private String itemSprite;

    //physical map region
    
    //space it takes in the inventory
    //x and y are set by inventory
    public Item(Region bounds, int state) {
        super(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());

        STATE = state;

        if(STATE == TABLE)
            setValid(false);
    }

    @Override
    public void paint(PaintEvent e){
        // if(){
        //     super.paint(e);
        // } else if(stashed){
        //     // e.getRenderer().render(getSprite(), getTX(), getTY(), getRenderOp(), getColorFilter());
        // }
    }

    @Override
    public void update(GameUpdateEvent e){
        super.update(e);

        // if(stashed){
        //     if(selected){
        //         setPos(e.mouseX(), e.mouseY());
        //     }
        // }
    }

    @Override
    public void gameClick(GameUpdateEvent e) {
        // if(stashed){
        //     selected = !selected;
        // }
    }

    @Override
    public void collide(Entity e) { 
        
    }
    
    public abstract Item switchState();

    //---------------  getters and setters -------------------------

    public boolean isStackable() {
        return this.stackable;
    }
    
    public String getItemSprite() {
        return this.itemSprite;
    }

    public void setItemSprite(String itemSprite) {
        this.itemSprite = itemSprite;
    }
}
