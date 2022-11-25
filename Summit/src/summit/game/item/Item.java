package summit.game.item;

import summit.game.GameUpdateEvent;
import summit.game.entity.Entity;
import summit.game.item.itemtable.ItemTable;
import summit.util.GameRegion;

public abstract class Item extends Entity{

    private boolean stackable = true;

    //picked up by the mouse
    private boolean floating = false;

    //if in inventory or not
    //true -> in an inventory or lootTable
    //false -> on the floor in the game
    private boolean stashed;

    //position and space it takes in the inventory
    public Item(float x, float y, float width, float height) {
        super(x, y, width, height);

        //yes
        super.setItems(null);
    }

    @Override
    public void gameClick(GameUpdateEvent e) {
        floating = true;
    }

    @Override
    public void collide(Entity e) { 
        
    }

    @Override
    public void destroy(GameUpdateEvent e){

    }
    
    //---------------  getters and setters -------------------------

    @Override
    public ItemTable getItems(){
        return null;
    }

    @Override
    public void setItems(ItemTable items){
        super.setItems(null);
        throw new Error("items in items in items...");
    }

    public boolean isStackable() {
        return this.stackable;
    }

    public void setStackable(boolean stackable) {
		this.stackable = stackable;
	}
    
    public boolean isStashed() {
        return this.stashed;
    }

    public void setStashed(boolean stashed) {
        this.stashed = stashed;
    }
}
