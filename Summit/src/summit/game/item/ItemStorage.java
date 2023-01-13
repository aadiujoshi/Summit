package summit.game.item;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import summit.game.GameUpdateEvent;
import summit.game.entity.Entity;

/**
 *
 * 
 * The ItemStorage class represents a container for items in the game. It
 * extends the HashMap class and
 * 
 * uses a string key to represent the sprite of the item and a value of a stack
 * of items. The class provides
 * 
 * methods for counting items, picking up items, reinitializing items, updating
 * item stacks, using items,
 * 
 * adding items, getting and setting the owner of the items and a toString
 * method for printing the contents
 * 
 * of the container.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R.
 */
public class ItemStorage extends HashMap<String, Stack<Item>> {

    // //string key is the sprite from Sprite class, with the value of stack of
    // items
    // private HashMap<String, Stack<Item>> items;

    private Entity owner;

    /**
     *
     * 
     * Creates a new instance of the ItemStorage class and sets the owner of the
     * items to the specified entity.
     * 
     * @param owner The entity that owns the items in the container
     */
    public ItemStorage(Entity owner) {
        this.owner = owner;
    }

    /**
     *
     * 
     * Counts the number of items with a specified sprite.
     * 
     * @param item The sprite of the item to count
     * @return The number of items with the specified sprite
     */
    public int countItem(String item) {
        return get(item).size();
    }

    /**
     *
     * 
     * Allows the owner of the container to pick up items from another container.
     * 
     * @param items2 The container of items to pick up
     */
    public void pickupItems(ItemStorage items2) {
        if (!owner.is(Entity.pickupItems))
            return;

        for (var itemStack : items2.entrySet()) {
            for (Item it : itemStack.getValue()) {
                if (it != null)
                    it.setOwner(owner);
            }

            if (get(itemStack.getKey()) != null)
                get(itemStack.getKey()).addAll(itemStack.getValue());

            itemStack.getValue().clear();
        }
    }

    /**
     *
     * 
     * Reinitializes all items in the container.
     */
    public void reinit() {
        for (Map.Entry<String, Stack<Item>> itemStack : this.entrySet()) {
            for (Item it : itemStack.getValue()) {
                it.reinit();
            }
        }
    }

    /**
     *
     * 
     * Updates the specified stack of items by removing all used items.
     * 
     * @param item The sprite of the item stack to update
     */
    public void updateStack(String item) {
        synchronized (this) {
            Stack<Item> s = get(item);
            for (int i = 0; i < s.size(); i++) {
                if (s.get(i).isUsed()) {
                    s.remove(i);
                    i--;
                    continue;
                }
            }
        }
    }

    /**
     *
     * 
     * Uses the top item in the specified stack of items.
     * 
     * @param item The sprite of the item stack to use
     */
    public void useItem(String item) {
        var s = get(item);
        if (s.isEmpty())
            return;

        s.peek().use();

        if (s.peek().isUsed())
            s.pop();
    }

    /**
     * 
     * Adds a specified number of copies of an item to the container.
     * 
     * @param copy The item to add to the container
     * @param num  The number of copies of the item to add
     */
    public void addItems(Item copy, int num) {
        // add item stack
        if (get(copy.getSprite()) == null)
            put(copy.getSprite(), new Stack<Item>());

        for (int i = 0; i < num; i++) {
            Item c = copy.copy();
            c.setOwner(owner);
            get(copy.getSprite()).push(c);
        }
    }

    /**
     * Returns the owner of the items in the container.
     * 
     * @return The owner of the items in the container
     */
    public Entity getOwner() {
        return this.owner;
    }

    /**
     * 
     * Sets the owner of all items in the container to the specified entity.
     * 
     * @param newOwner The new owner of the items in the container
     */
    public void setOwner(Entity newOwner) {
        this.owner = newOwner;

        for (var itemstack : this.entrySet()) {
            for (var item : itemstack.getValue()) {
                item.setOwner(newOwner);
            }
        }
    }

    /**
     * 
     * Returns a string representation of the contents of the container, including
     * the number of items
     * with each sprite.
     * 
     * @return A string representation of the contents of the container
     */
    @Override
    public String toString() {
        String str = "";

        str += "[Items: ";

        int i = 0;
        for (var stack : entrySet()) {
            i++;
            str += stack.getKey() + ":" + stack.getValue().size();
            if (i != entrySet().size())
                str += ", ";
        }

        str += "]";

        return str;
    }
}
