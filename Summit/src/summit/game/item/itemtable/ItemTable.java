package summit.game.item.itemtable;

import java.util.ArrayList;

import summit.game.GameMap;
import summit.game.item.Item;
import summit.util.GameRegion;

public class ItemTable{

    private ArrayList<Item> items;

    private GameRegion location;

    public ItemTable(GameRegion location){
        items = new ArrayList<>();
    }

    public boolean addItem(Item e){
        items.add(e);
        return true;
    }

    public void drop(GameMap map){
        for(int i = 0; i < items.size(); i++){
            Item it = items.get(i);
            it.setStashed(false);
            it.setPos(location.getX(), location.getY());
            map.spawn(items.get(i));
        }

        items.clear();
    }
    
    public ArrayList<Item> getItems() {
        return this.items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }
}