package summit.game.item;

import java.io.Serializable;

import summit.game.entity.Entity;

/**
 *
 * 
 * The Item abstract class represents an item in the game. It provides a basic
 * 
 * implementation for properties and methods
 * 
 * common to all items, such as the owner of the item, the sprite of the item,
 * and
 * 
 * the ability to use the item.
 * 
 * Subclasses of this class should implement the {@link #copy()} method to
 * return
 * 
 * a copy of the item and the {@link #use()} method to define
 * 
 * the specific behavior of the item when used.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R. S.
 */
public abstract class Item implements Serializable {
    private String sprite;

    // if this item can be used by the enitity
    private boolean used;

    private String textName = getClass().getSimpleName();

    private final String TYPE = getClass().getSimpleName();

    private Entity owner;

    /**
     *
     * 
     * Creates a new instance of the Item class and sets the owner of the item to
     * the specified entity.
     * 
     * @param owner The entity that owns this item
     */
    public Item(Entity owner) {
        this.owner = owner;
    }

    
    /** 
     * @param reinit(
     * @return Item
     */
    /**
     *
     * 
     * Creates a copy of the current item.
     * 
     * @return A copy of the current item.
     */
    public abstract Item copy();

    /**
     * Reinitialize the item with the new owner
     */
    public void reinit() {
    }

    /**
     *
     * 
     * Uses the item. The specific behavior of the item when used should be defined
     * in the implementation of this method
     * in the subclass.
     */
    public void use() {
        setUsed(true);
    }

    /**
     *
     * 
     * Returns the type of the item.
     * 
     * @return the type of the item
     */
    public String getType() {
        return this.TYPE;
    }

    /**
     *
     * 
     * Returns the sprite of the item.
     * 
     * @return the sprite of the item
     */
    public String getSprite() {
        return this.sprite;
    }

    /**
     *
     * 
     * Sets the sprite of the item.
     * 
     * @param sprite the new sprite of the item
     */
    public void setSprite(String sprite) {
        this.sprite = sprite;
    }

    /**
     *
     * 
     * Returns the owner of the item.
     * 
     * @return the owner of the item
     */
    public Entity getOwner() {
        return this.owner;
    }

    /**
     *
     * 
     * Sets the owner of the current item to the specified Entity.
     * 
     * @param owner The Entities that will own this item
     */
    public void setOwner(Entity owner) {
        this.owner = owner;
    }

    /**
     * Returns whether or not the item has been used.
     * 
     * @return True if the item has been used, false otherwise.
     */
    public boolean isUsed() {
        return this.used;
    }

    /**
     * Sets the used status of the item.
     * 
     * @param used The new used status of the item.
     */
    public void setUsed(boolean used) {
        this.used = used;
    }

    /**
     * Returns the text name of the item.
     * 
     * @return The text name of the item.
     */
    public String getTextName() {
        return this.textName;
    }

    /**
     * Sets the text name of the item.
     * 
     * @param textName The new text name of the item.
     */
    public void setTextName(String textName) {
        this.textName = textName;
    }

    /**
     * Returns a string representation of the item.
     * 
     * @return A string representation of the item.
     */
    @Override
    public String toString() {
        return TYPE;
    }
}
