/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.gui;

import summit.deprecated.item.Item;
import summit.deprecated.item.itemtable.Inventory;
import summit.deprecated.item.itemtable.ItemTable;
import summit.gfx.PaintEvent;
import summit.gfx.Sprite;

@Deprecated
public class ItemGUI extends Container{

    //ref to actual player inventory
    private ItemTable items;
    
    public ItemGUI(Inventory inv) {
        super(null, null, 0.5f, 0.5f, Sprite.INVENTORY);
        this.items = inv;
    }
    
    @Override
    public void paint(PaintEvent e){
        super.paint(e);

        for(Item item : items.getItems()){
            item.paint(e);
        }
    }
}
