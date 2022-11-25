package summit.game.entity;

import summit.game.GameMap;
import summit.game.GameUpdateEvent;
import summit.game.GameUpdateReciever;
import summit.game.item.Item;
import summit.game.item.itemtable.ItemTable;
import summit.game.tile.Tile;
import summit.gfx.Light;
import summit.gfx.OrderPaintEvent;
import summit.gfx.PaintEvent;
import summit.gfx.RenderLayers;
import summit.util.GameRegion;
import summit.util.Time;

public abstract class Entity extends GameRegion implements GameUpdateReciever{

    private float dx, dy;
    //knockback lasts for 2 seconds
    private float kx, ky;

    private float lx, ly;

    private Light shadow;

    //just metadata for class name
    private final String NAME = getClass().getSimpleName();

    private ItemTable items;

    private Tile onTile;

    private float hitDamage;
    private float damageResistance;

    private float maxHealth;
    private float health;
    private boolean destroyed;

    private boolean invulnerable;
    private boolean fireResistant;
    private boolean inWater;
    private boolean onFire;
    private boolean moving;

    public Entity(float x, float y, float width, float height){
        super(x,y,width,height);
        super.setRLayer(RenderLayers.STRUCTURE_ENTITY_LAYER);

        this.maxHealth = 1;
        this.health = maxHealth;
        this.hitDamage = 0;
        this.damageResistance = 0;

        this.lx = x;
        this.ly = y;
        
        Light sdw = new Light(x, y, 1f, -50, -50, -50);
        sdw.setRenderLayer(RenderLayers.STRUCTURE_ENTITY_LAYER-1);
        this.shadow = sdw;
    }

    @Override
    public void setRenderLayer(OrderPaintEvent ope){
        super.setRenderLayer(ope);
        shadow.setRenderLayer(ope);
    }

    @Override
    public void paint(PaintEvent e){
        super.paint(e);
    }

    @Override
    public void update(GameUpdateEvent e){
        Entity contact = e.getMap().entityAt(getX(), getY());
        if(contact != null){
            if(contact instanceof Item){
                pickup((Item)contact);
            }
        }

        onTile = e.getMap().getTileAt(getX(), getY());
        onTile.collide(this);
        
        updateMoving();
    }

    public void pickup(Item contact) {
        contact.setStashed(true);
        items.addItem(contact);
    }

    public void damage(float damage, Entity e){
        health -= damage;

        if(health <= 0)
            destroyed = true;
    }

    //called by parent object when set to destroy
    abstract public void destroy(GameUpdateEvent ge);

    public boolean moveTo(GameMap map, float newX, float newY){
        if(map.getTileAt(newX, newY) == null || 
            map.getTileAt(newX, newY).isBoundary()){

            moving = false;
            return false;
        }

        moving = true;
        return true;
    }

    private void updateMoving(){
        if(lx != getX() || ly != getY()){
            moving = true;
            lx = getX();
            ly = getY();
        } else {
            moving = false;
        }
    }
    
    //---------------------------------------------------------
    //getters and setters
    //---------------------------------------------------------

    @Override 
    public void setX(float x){
        super.setX(x);
        if(shadow != null){
            shadow.setX(x);
        }
        updateMoving();
    }

    @Override 
    public void setY(float y){
        super.setY(y);
        if(shadow != null){
            shadow.setY(y);
        }
        updateMoving();
    }  

    public float getDx() {
        return this.dx;
    }

    public void setDx(float dx) {
        this.dx = dx;
    }

    public float getDy() {
        return this.dy;
    }

    public void setDy(float dy) {
        this.dy = dy;
    }

    public boolean isMoving() {
        return this.moving;
    }
    
    public String getName(){
        return this.NAME;
    }
    
    public boolean inWater() {
        return this.inWater;
    }
    
    public boolean destroyed(){
        return destroyed;
    }

    public void setDestroyed(boolean d){
        this.destroyed = d;
    }

    public void setInWater(boolean inWater) {
        this.inWater = inWater;
    }
    
    public float getHealth() {
        return this.health;
    }

    public void setHealth(float health) {
        this.health = health;
    }
    
    public void changeHealth(float c){
        this.health += c;
    }

    public boolean onFire(){
        return this.onFire;
    }

    public void setOnFire(boolean onFire){
        this.onFire = onFire;
    }
    
    public float getMaxHealth() {
        return this.maxHealth;
    }

    public void setMaxHealth(float maxHealth) {
        this.maxHealth = maxHealth;
    }
    
    public Tile getOnTile(){
        return this.onTile;
    }
    
    public boolean isInvulnerable() {
        return this.invulnerable;
    }

    public void setInvulnerable(boolean invulnerable) {
        this.invulnerable = invulnerable;
    }

    public float getHitDamage() {
        return this.hitDamage;
    }

    public void setHitDamage(float hitDamage) {
        this.hitDamage = hitDamage;
    }

    public void setKnockback(float kx, float ky){
        this.kx = kx;
        this.ky = ky;
    }
    
    public ItemTable getItems() {
        return this.items;
    }

    public void setItems(ItemTable items) {
        this.items = items;
    }
    
    public float getDamageResistance() {
        return this.damageResistance;
    }

    public void setDamageResistance(float damageResistance) {
        this.damageResistance = damageResistance;
    }    

    public boolean isFireResistant() {
        return this.fireResistant;
    }

    public void setFireResistant(boolean fireResistant) {
        this.fireResistant = fireResistant;
    }

}