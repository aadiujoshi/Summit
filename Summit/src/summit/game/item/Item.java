package summit.game.item;

import summit.game.GameUpdateEvent;
import summit.game.entity.Entity;
import summit.gfx.PaintEvent;
import summit.util.Region;

public abstract class Item extends Entity{

    //if in inventory or not
    //true -> in an inventory or lootTable
    //false -> on the floor in the game
    private boolean stashed;

    //picked up by the mouse
    private boolean selected = false;

    //can stack on other of the same items
    private boolean stackable = true;

    //the space the item takes in the item table
    private Region tableRegion;
    
    //itemtable it belongs to
    // private ItemGUI paintRegion;

    //sprite thats rendered in the inventory/gui
    private String itemSprite;

    //physical map region
    
    //space it takes in the inventory
    //x and y are set by inventory
    public Item(Region gameRegion, Region tableRegion) {
        super(gameRegion.getX(), gameRegion.getY(), gameRegion.getWidth(), gameRegion.getHeight());
        
        this.tableRegion = tableRegion;
    }

    @Override
    public void paint(PaintEvent e){
        if(!stashed){
            super.paint(e);
        } else if(stashed){
            e.getRenderer().render(getSprite(), getTX(), getTY(), getRenderOp(), getColorFilter());
        }
    }

    @Override
    public void update(GameUpdateEvent e){
        super.update(e);

        if(stashed){
            if(selected){
                setTablePos(e.mouseX(), e.mouseY());
            }
        }
    }

    @Override
    public void gameClick(GameUpdateEvent e) {
        if(stashed){
            selected = !selected;
        }
    }

    @Override
    public void collide(Entity e) { 
        
    }

    @Override
    public void destroy(GameUpdateEvent e){

    }
    
    //---------------  getters and setters -------------------------

    public void setTablePos(int x, int y){
        this.tableRegion.setPos(x, y);
    }

    public int getTX(){
        return (int)this.tableRegion.getX();
    }

    public void setTX(int x){
        this.tableRegion.setX(x);
    }

    public int getTY(){
        return (int)this.tableRegion.getY();
    }

    public void setTY(int y){
        this.tableRegion.setY(y);
    }

    public void setTWidth(int width){
        this.tableRegion.setWidth(width);
    }

    public int getTWidth(){
        return (int)this.tableRegion.getWidth();
    }
    
    public void setTHeight(int height){
        this.tableRegion.setHeight(height);
    }

    public int getTHeight(){
        return (int)this.tableRegion.getWidth();
    }

    public boolean isStackable() {
        return this.stackable;
    }

    public void unstash(){
        this.stashed = false;
    }

    // public void setItemTable(ItemTable table){
    //     if(!table.addItem(this)){
    //         return;
    //     }

    //     this.paintRegion = table;
    //     this.stashed = true;
    // }

    public boolean isStashed() {
        return this.stashed;
    }

    public void setStashed(boolean stashed) {
        this.stashed = stashed;
        this.selected = false;
    }
    
    public String getItemSprite() {
        return this.itemSprite;
    }

    public void setItemSprite(String itemSprite) {
        this.itemSprite = itemSprite;
    }
}
