package summit.game.item;

import java.io.Serializable;

import summit.game.GameUpdateEvent;
import summit.game.entity.Entity;
import summit.game.entity.mob.MobEntity;

public abstract class Item implements Serializable{
    private String sprite;

    //if this item can be used by the enitity
    private boolean used;

    private String textName = getClass().getSimpleName();

    private final String TYPE = getClass().getSimpleName();

    private Entity owner;

    public Item(Entity owner){
        this.owner = owner;
    }

    public abstract Item copy();
    
    public void reinit(){}

    public void use() {
        setUsed(true);
    }

    public String getType(){
        return this.TYPE;
    }
    
    public String getSprite() {
        return this.sprite;
    }

    public void setSprite(String sprite) {
        this.sprite = sprite;
    }
    
    public Entity getOwner() {
        return this.owner;
    }
    
    public void setOwner(Entity owner) {
        this.owner = owner;
    }
    
    public boolean isUsed() {
        return this.used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
    
    public String getTextName() {
        return this.textName;
    }

    public void setTextName(String textName) {
        this.textName = textName;
    }
}
