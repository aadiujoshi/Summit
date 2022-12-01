/*
* BPA project by Aadi Joshi, Aditya Bhattaharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.item.itemtable;

import summit.game.item.Item;
import summit.util.GameRegion;

/**
 * Item adding is filtered in Inventory compared to unbounded ItemTable
 */
public class Inventory extends ItemTable{

    private int width, height;

    //location is the owner of the itemtable
    public Inventory(GameRegion location, int width, int height){
        super(location);
        
        this.width = width;
        this.height = height;
    }

    /**
     * Uses the Items position to determine if it can be added to the inventory, 
     * or if it is stackable and can be added to a stack
     * </p> 
     * 
     * DOES NOT WORK
     * 
     * @param Item to be inserted
     * @return If the item can be added
     */
    public boolean vacantSpace(Item e){
        for (Item other : super.getItems()) {
            if(other.overlap(e))
                return false;
        }
        return true;
    }

    @Override
    public boolean addItem(Item e){
        // e.setTablePos(1, 1);
        super.addItem(e);
        return true;
    }

    public int getWidth(){
        return this.width;
    }

    public int getHeight(){
        return this.height;
    }
}
