package summit.game.item;

import java.io.Serializable;

import summit.game.entity.mob.MobEntity;

public abstract class Item implements Serializable{
    private String sprite;

    //if this item can be used by the enitity
    private boolean used;

    private final String TYPE = getClass().getSimpleName();

    private MobEntity owner;

    public Item(MobEntity owner){
        this.owner = owner;
    }

    public abstract Item copy();

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
    
    public MobEntity getOwner() {
        return this.owner;
    }
    
    public boolean isUsed() {
        return this.used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}
