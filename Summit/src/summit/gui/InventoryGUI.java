/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import summit.game.item.Item;
import summit.game.item.ItemStorage;
import summit.gfx.ColorFilter;
import summit.gfx.PaintEvent;
import summit.gfx.Renderer;
import summit.gfx.Sprite;

public class InventoryGUI extends Container{

    //ref to actual player inventory
    private ItemStorage items;
    
    //a little bit of hardcoding never hurt anyone
    private float[] item_x = new float[]{0.25f, 0.25f, 0.25f, 
                                            0.5f, 0.5f, 0.5f, 
                                            0.75f, 0.75f, 0.75f};
    private float[] item_y = new float[]{0.25f, 0.5f, 0.75f,
                                            0.25f, 0.5f, 0.75f,
                                            0.25f, 0.5f, 0.75f};
                                            
    public InventoryGUI(ItemStorage inv) {
        super(null, null, 0.5f, 0.5f, Sprite.INVENTORY);
        this.items = inv;
        super.setOutline(false);
    }
    
    @Override
    public void paint(PaintEvent e){
        super.paint(e);

        int i = 0;
        for(Map.Entry<String, Stack<Item>> item : items.entrySet()){
            if(item.getKey().contains("key"))
                continue;

            int n = item.getValue().size();

            int px = (int)(getX()-getWidth()/2) + (int)(getWidth()*item_x[i]);
            int py = (int)(getY()-getHeight()/2) + (int)(getHeight()*item_y[i]);

            e.getRenderer().render(item.getKey(), px, py, Renderer.NO_OP, ColorFilter.NOFILTER);

            e.getRenderer().renderText(n+"", px+4, py+4, Renderer.NO_OP, new ColorFilter(255, 255, 255));

            i++;
        }
    }
}
