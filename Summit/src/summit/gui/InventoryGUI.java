package summit.gui;

import java.util.ArrayList;

import summit.game.item.Item;
import summit.game.item.itemtable.Inventory;
import summit.gfx.PaintEvent;
import summit.gfx.Renderer;
import summit.gfx.Sprite;

public class InventoryGUI extends Container{

    //ref to actual player inventory
    private Inventory inventory;

    public InventoryGUI(Inventory inv) {
        super(null, 0.5f, 0.5f, 0.5625f, (5f/9f));
        this.inventory = inv;
    }
    
    @Override
    public void paint(PaintEvent e){
        e.getRenderer().render(Sprite.INVENTORY, getX(), getY(), Renderer.NO_OP, null);

        for(Item item : inventory.getItems()){
            item.paint(e);
        }
    }
}
