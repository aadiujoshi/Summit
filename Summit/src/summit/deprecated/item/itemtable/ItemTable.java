/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.deprecated.item.itemtable;

import java.io.Serializable;
import java.util.ArrayList;

import summit.deprecated.item.Item;
import summit.game.gamemap.GameMap;
import summit.util.GameRegion;

@Deprecated
public class ItemTable implements Serializable{

    private ArrayList<Item> items;

    private GameRegion location;

    public ItemTable(GameRegion location){
        items = new ArrayList<>();
    }

    public boolean canAdd(Item e){
        return true;
    }

    public boolean addItem(Item e){
        if(canAdd(e)){
            items.add(e);
            return true;
        }
        return false;
    }

    public void drop(GameMap map){
        for(int i = 0; i < items.size(); i++){
            Item it = items.get(i);
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