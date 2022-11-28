package summit.game.entity;

import summit.game.GameMap;
import summit.game.GameUpdateEvent;
import summit.game.GameUpdateReciever;
import summit.game.animation.ScheduledEvent;
import summit.game.animation.Scheduler;
import summit.game.item.Item;
import summit.game.item.itemtable.ItemTable;
import summit.game.tile.Tile;
import summit.gfx.ColorFilter;
import summit.gfx.Light;
import summit.gfx.OrderPaintEvent;
import summit.gfx.PaintEvent;
import summit.gfx.RenderLayers;
import summit.util.GameRegion;

public abstract class Entity extends GameRegion implements GameUpdateReciever{
    private float dx, dy;

    //knockback lasts for 2 seconds
    private Knockback kb;

    private float lx, ly;

    private Light shadow;

    //just metadata for class name
    private final String NAME = getClass().getSimpleName();

    private ItemTable items;

    private Tile onTile;

    private boolean pickupItems;

    private float hitDamage;
    private float damageResistance;
    private boolean hitCooldown;

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
        GameMap map = e.getMap();

        if(health <= 0){
            destroyed = true;
            return;
        }

        if(kb != null){
            kb.move(e);
            if(kb.finished())
                this.kb = null;
        }

        Entity contact = map.entityAt(getX(), getY());

        if(contact != null){
            if(contact instanceof Item){
                pickup((Item)contact);
            }
        }

        onTile = map.getTileAt(getX(), getY());
        onTile.collide(this);
        
        updateMoving();
    }

    public void pickup(Item contact) {
        contact.setStashed(true);
        items.addItem(contact);
    }

    public void damage(Entity hitBy){
        if(hitBy.hitCooldown())
            return;

        health -= hitBy.getHitDamage();
        
        this.setKnockback(20, 500, hitBy);

        //flash animation-----------------------------------------
        Scheduler.registerEvent(new ScheduledEvent(125,8) {
            private boolean flash = false;
            @Override
            public void run() {
                if(!flash){
                    setColorFilter(new ColorFilter(150, 0, 0));
                    flash = true;
                } else{
                    setColorFilter(ColorFilter.NOFILTER);
                    flash = false;
                }
            }
        });
        //---------------------------------------------------------

        hitBy.setHitCooldown(true);

        if(health <= 0)
            destroyed = true;
    }

    //called by parent object when set to destroy
    public void destroy(GameUpdateEvent ge){
        //code to drop item table
    }

    public boolean moveTo(GameMap map, float newX, float newY){
        if(map.getTileAt(newX, newY) == null || 
            map.getTileAt(newX, newY).isBoundary()){
            return false;
        }

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
            shadow.setX(x + getXOffset());
        }
        updateMoving();
    }

    @Override 
    public void setY(float y){
        super.setY(y);
        if(shadow != null){
            shadow.setY(y + getYOffset());
        }
        updateMoving();
    }  

    public float getDx() {
        return this.dx / (inWater() ? 2 : 1);
    }

    public void setDx(float dx) {
        this.dx = dx;
    }

    public float getDy() {
        return this.dy / (inWater() ? 2 : 1);
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
        if(health <= 0)
            setDestroyed(true);
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

    private void setKnockback(float k, int durationMS, Entity hitBy){
        this.kb = new Knockback(k, this, hitBy, durationMS);
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
    
    public boolean hitCooldown() {
        return this.hitCooldown;
    }
    
    public boolean canPickupItems() {
        return this.pickupItems;
    }

    public void setPickupItems(boolean canPickupItems) {
        this.pickupItems = canPickupItems;
    }

    public void setHitCooldown(boolean hitCooldown) {
        this.hitCooldown = hitCooldown;

        if(hitCooldown){
            Scheduler.registerEvent(new ScheduledEvent(500,1) {
                @Override
                public void run() {
                    setHitCooldown(false);
                }
            });
        }
    }
}