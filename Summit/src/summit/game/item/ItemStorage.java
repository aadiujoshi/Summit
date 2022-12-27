package summit.game.item;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import summit.game.GameUpdateEvent;
import summit.game.entity.Entity;

public class ItemStorage extends HashMap<String, Stack<Item>>{
    
    // //string key is the sprite from Sprite class, with the value of stack of items
    // private HashMap<String, Stack<Item>> items;

    private Entity owner;

    public ItemStorage(Entity owner){
        this.owner = owner;
    }

    public int countItem(String item){
        return get(item).size();
    }

    public void pickupItems(ItemStorage items2){
        if(!owner.is(Entity.pickupItems))
            return;
        
        for (var itemStack : items2.entrySet()) {
            for (Item it : itemStack.getValue()) {
                if(it != null)
                    it.setOwner(owner);
            }

            if(get(itemStack.getKey()) != null)
                get(itemStack.getKey()).addAll(itemStack.getValue());
            
            itemStack.getValue().clear();
        }
    }

    public void reinit(){
        for (Map.Entry<String, Stack<Item>> itemStack : this.entrySet()) {
            for (Item it : itemStack.getValue()) {
                it.reinit();
            }
        }
    }

    public void updateStack(String item){
        synchronized(this){
            Stack<Item> s = get(item);
            for (int i = 0; i < s.size(); i++) {
                if(s.get(i).isUsed()){
                    s.remove(i);
                    i--;
                    continue;
                }
            }
        }
    }

    public void useItem(String item){
        var s = get(item);
        if(s.isEmpty())
            return;

        s.peek().use();

        if(s.peek().isUsed())
            s.pop();
    }

    /**
     * used when initializing an entity, NOT for recieving items
     * @param copy
     * @param num
     */
    public void addItems(Item copy, int num){
        //add item stack
        if(get(copy.getSprite()) == null)
            put(copy.getSprite(), new Stack<Item>());

        for(int i = 0; i < num; i++){
            Item c = copy.copy();
            c.setOwner(owner);
            get(copy.getSprite()).push(c);
        }
    }

    public Entity getOwner() {
        return this.owner;
    }

    public void setOwner(Entity newOwner) {
        this.owner = newOwner;

        for (var itemstack : this.entrySet()) {
            for (var item : itemstack.getValue()) {
                item.setOwner(newOwner);
            }
        }
    }
}
