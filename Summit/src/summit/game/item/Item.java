package summit.game.item;

import summit.game.GameUpdateEvent;
import summit.game.entity.Entity;
import summit.game.item.itemtable.ItemTable;
import summit.gfx.PaintEvent;
import summit.util.GameRegion;
import summit.util.Region;

public abstract class Item extends Entity{

    //if in inventory or not
    //true -> in an inventory or lootTable
    //false -> on the floor in the game
    private boolean stashed;

    //picked up by the mouse
    private boolean floating = false;

    //can stack on other of the same items
    private boolean stackable = true;

    //the space the item takes in the item table
    private Region tableRegion;

    //itemtable it belongs to
    private ItemTable itemTable;

    //space it takes in the inventory
    public Item(float width, float height) {
        super(0, 0, width, height);
        
        this.tableRegion = new Region(0, 0, width, height);
    }

    //place on the map
    public Item(float x, float y, float width, float height){
        super(x, y, width, height);
    }

    @Override
    public void paint(PaintEvent e){
        
    }

    @Override
    public void gameClick(GameUpdateEvent e) {
        if(stashed){
            floating = !floating;
        }
    }

    @Override
    public void collide(Entity e) { 
        
    }

    @Override
    public void destroy(GameUpdateEvent e){

    }
    
    //---------------  getters and setters -------------------------

    public void setTablePos(float x, float y){
        this.tableRegion.setPos(x, y);
    }

    public float getTX(){
        return this.tableRegion.getX();
    }

    public void setTX(float x){
        this.tableRegion.setX(x);
    }

    public float getTY(){
        return this.tableRegion.getY();
    }

    public void setTY(float y){
        this.tableRegion.setY(y);
    }

    public void setTWidth(float width){
        this.tableRegion.setWidth(width);
    }

    public float getTWidth(){
        return this.tableRegion.getWidth();
    }
    
    public void setTHeight(float height){
        this.tableRegion.setHeight(height);
    }

    public float getTHeight(){
        return this.tableRegion.getWidth();
    }

    public boolean isStackable() {
        return this.stackable;
    }

    public void unstash(){
        this.stashed = false;
        setItemTable(null);
    }

    public void setItemTable(ItemTable table){
        if(!table.addItem(this)){
            return;
        }

        this.itemTable = table;
        this.stashed = true;
    }

    public boolean isStashed() {
        return this.stashed;
    }

    public void setStashed(boolean stashed) {
        this.stashed = stashed;
        this.floating = false;
    }
}
