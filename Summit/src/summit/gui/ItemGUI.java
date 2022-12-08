/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.gui;

import java.io.Serializable;
import java.util.ArrayList;

import summit.game.item.Item;
import summit.game.item.itemtable.Inventory;
import summit.game.item.itemtable.ItemTable;
import summit.gfx.PaintEvent;
import summit.gfx.Renderer;
import summit.gfx.Sprite;

public class ItemGUI extends Container{

    //ref to actual player inventory
    private ItemTable items;
    
    public ItemGUI(Inventory inv) {
        super(null, 0.5f, 0.5f, Sprite.INVENTORY);
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
