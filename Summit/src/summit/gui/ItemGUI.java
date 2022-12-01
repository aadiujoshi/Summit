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

    private String guiSprite;

    public ItemGUI(Inventory inv) {
        super(null, 0.5f, 0.5f, 0.5625f, (5f/9f));
        this.items = inv;
    }
    
    @Override
    public void paint(PaintEvent e){
        e.getRenderer().render(Sprite.INVENTORY, getX(), getY(), Renderer.NO_OP, null);

        for(Item item : items.getItems()){
            item.paint(e);
        }
    }

    public String getGuiSprite() {
        return this.guiSprite;
    }

    public void setGuiSprite(String guiSprite) {
        this.guiSprite = guiSprite;
    }
}
