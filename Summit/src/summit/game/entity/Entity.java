/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import summit.game.GameUpdateEvent;
import summit.game.animation.ParticleAnimation;
import summit.game.entity.projectile.Projectile;
import summit.game.gamemap.GameMap;
import summit.game.item.Item;
import summit.game.item.WeaponItem;
import summit.gfx.ColorFilter;
import summit.gfx.Light;
import summit.gfx.OrderPaintEvent;
import summit.gfx.PaintEvent;
import summit.gfx.RenderLayers;
import summit.gfx.Sprite;
import summit.util.Direction;
import summit.util.GameRegion;
import summit.util.ScheduledEvent;
import summit.util.Scheduler;

public abstract class Entity extends GameRegion{

    //string key is the sprite from Sprite class, with the value of stack of items
    private HashMap<String, Stack<Item>> items;

    //currently equipped weapon
    private WeaponItem equipped;

    //the map this entity belongs to
    private String curMap = " ";
    
    private float dx, dy;

    //knockback lasts for 2 seconds
    private Knockback kb;

    //last x and last y; used to check if entity is moving
    private float lx, ly;

    //facing of the entity
    private Direction facing;

    private Light shadow;

    private float projDamage;

    private float attackDamage;
    private int damageCooldownMS;

    private float attackRange;
    private int attackCooldownMS;

    //coefficient
    private float damageResistance;

    private float maxHealth;
    private float health;

    public Entity(float x, float y, float width, float height){
        super(x,y,width,height);
        super.setRLayer(RenderLayers.STRUCTURE_ENTITY_LAYER);

        this.maxHealth = 1;
        this.health = maxHealth;
        this.attackDamage = 0;
        this.projDamage = 1;
        this.attackRange = 2;
        this.damageResistance = 0;

        this.lx = x;
        this.ly = y;
        
        this.shadow = new Light(x, y, 1f, -50, -50, -50);

        this.items = new HashMap<>();

        items.put(Sprite.ARROW_ITEM, new Stack<Item>());
        items.put(Sprite.SNOWBALL, new Stack<Item>());
        items.put(Sprite.APPLE_ITEM, new Stack<Item>());
        items.put(Sprite.STICK_ITEM, new Stack<Item>());
        items.put(Sprite.BONE_ITEM, new Stack<Item>());
        items.put(Sprite.GOLD_COIN, new Stack<Item>());

        this.add(attackCooldown);
        this.add(damageCooldown);
        this.add(destroyed);
        this.add(invulnerable);
        this.add(fireResistant);
        this.add(inWater);
        this.add(onFire);
        this.add(moving);
        this.add(projectileDamage);
        this.add(pickupItems);

        this.set(projectileDamage, true);
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
        curMap = e.getMap().getName();

        if(health <= 0){
            set(destroyed, true);
            return;
        }

        if(kb != null){
            kb.update(e);
            if(kb.finished())
                this.kb = null;
        }
        updateMoving();
    }

    @Override
    public void collide(Entity e){
        if(e.is(onFire))
            set(onFire, true);
    }

    public void damage(Entity hitBy){
        if(hitBy.is(attackCooldown) || this.is(damageCooldown))
            return;
        if(hitBy instanceof Projectile && !is(projectileDamage))
            return;

        health -= hitBy.getAttackDamage();
        
        this.setKnockback(10, 500, hitBy);

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
        
        set(damageCooldown, true);
        
        if(health <= 0){
            set(destroyed, true);
        }
    }


    @Override
    public void gameClick(GameUpdateEvent e){
        
    }

    public void attack(Entity e, GameUpdateEvent ev){
        e.damage(this);
        
        if(!is(attackCooldown))
            set(attackCooldown, true);
    }

    public void destroy(GameUpdateEvent e){
        e.getMap().addAnimation(
                new ParticleAnimation(getX(), getY(), 
                                        300, 
                                        20, 
                                        getColor()));
    }
    
    public boolean moveTo(GameMap map, float newX, float newY){
        if(map.getTileAt(newX, newY) == null || 
            map.getTileAt(newX, newY).isBoundary()){
            return false;
        }

        return true;
    }

    public boolean lineOfSight(Entity e, GameMap map){

        //vertically aligned
        if(e.getX() == getX()){
            float start = Math.min(getY(), e.getY());
            float end = Math.max(getY(), e.getY());

            for(float y = start; y <= end; y++){
                if(map.getTileAt(getX(), y).isBoundary()){
                    return false;
                }
            }
            return true;
        }

        float m = (e.getY()-getY()) / (e.getX()-getX());
        float b = -1*((float)m*getX()-getY());

        float start = Math.min(getX(), e.getX());
        float end = Math.max(getX(), e.getX());

        for(float inc_x = start; inc_x < end; inc_x += (1f/map.getWidth())){
            if(map.getTileAt( inc_x , (inc_x*m + b)).isBoundary()){
                return false;
            }
        }
        return true;
    }

    private void updateMoving(){
        if(lx != getX() || ly != getY()){
            this.set(moving, true);
        } else {
            this.set(moving, false);
        }

        //fix facing
        if(getX() > lx){
            facing =  Direction.EAST;
        }
        if(getX() < lx){
            facing = Direction.WEST;
        }
        if(getY() > ly){
            facing = Direction.NORTH;
        }
        if(getY() < ly){
            facing = Direction.SOUTH;
        }
        
        lx = getX();
        ly = getY();
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

    @Override
    public void set(String property, boolean value){
        super.set(property, value);

        switch(property){
            case attackCooldown -> {
                if(is(attackCooldown)){
                    Scheduler.registerEvent(new ScheduledEvent(attackCooldownMS,1) {
                        @Override
                        public void run() {
                            set(attackCooldown, false);
                        }
                    });
                }

                break;
            }

            case damageCooldown -> {
                if(is(damageCooldown)){
                    Scheduler.registerEvent(new ScheduledEvent(damageCooldownMS,1) {
                        @Override
                        public void run() {
                            set(damageCooldown, false);
                        }
                    });
                }

                break;
            }
        }
    }

    public float getDx() {
        return this.dx / (is(inWater) ? 2 : 1);
    }

    public void setDx(float dx) {
        this.dx = dx;
    }

    public float getDy() {
        return this.dy / (is(inWater) ? 2 : 1);
    }

    public void setDy(float dy) {
        this.dy = dy;
    }
    
    public float getHealth() {
        return this.health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public void modHealth(float c){
        this.health += c;
    }
    
    public float getMaxHealth() {
        return this.maxHealth;
    }

    public void setMaxHealth(float maxHealth) {
        this.maxHealth = maxHealth;
    }
    
    public float getAttackDamage() {
        return this.attackDamage;
    }

    public void setAttackDamage(float hitDamage) {
        this.attackDamage = hitDamage;
    }

    public void setKnockback(float k, int durationMS, Entity hitBy){
        this.kb = new Knockback(k, this, hitBy, durationMS);
    }
    
    public float getDamageResistance() {
        return this.damageResistance;
    }
    
    public Light getShadow() {
        return this.shadow;
    }

    public void setShadow(Light shadow) {
        shadow.setRenderLayer(RenderLayers.STRUCTURE_ENTITY_LAYER-1);
        this.shadow = shadow;
    }

    public void setDamageResistance(float damageResistance) {
        this.damageResistance = damageResistance;
    }    
    
    public Direction getFacing() {
        return this.facing;
    }

    public void setFacing(Direction facing) {
        this.facing = facing;
    }
    
    public String getCurMap() {
        return this.curMap;
    }

    public float getAttackRange() {
        return this.attackRange;
    }

    public void setAttackRange(float attackRange) {
        this.attackRange = attackRange;
    }

    public int getAttackCooldownMS() {
        return this.attackCooldownMS;
    }

    public void setAttackCooldownMS(int attackCooldownMS) {
        this.attackCooldownMS = attackCooldownMS;
    }

    public int getDamageCooldownMS() {
        return this.damageCooldownMS;
    }

    public void setDamageCooldownMS(int hitCooldownMS) {
        this.damageCooldownMS = hitCooldownMS;
    }
    
    public float getProjDamage() {
        return this.projDamage;
    }

    public void setProjDamage(float projDamage) {
        this.projDamage = projDamage;
    }
    
    public WeaponItem getEquipped() {
        return this.equipped;
    }

    public void setEquipped(WeaponItem equipped) {
        this.equipped = equipped;
    }

    public HashMap<String, Stack<Item>> getItems(){
        return this.items;
    }
    
    public void pickupItems(HashMap<String, Stack<Item>> items2){
        if(!is(pickupItems))
            return;
        
        for (var itemStack : items2.entrySet()) {
            for (Item it : itemStack.getValue()) {
                it.setOwner(this);
            }

            items.get(itemStack.getKey()).addAll(itemStack.getValue());
            itemStack.getValue().clear();
        }
    }

    public void useItem(String item){
        var s = items.get(item);
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
        
        for(int i = 0; i < num; i++){
            Item c = copy.copy();
            c.setOwner(this);
            items.get(copy.getSprite()).push(c);
        }
    }

    //-----------  game tag / property keys ------------------------------

    public static final String attackCooldown = "hitCooldown";
    public static final String damageCooldown = "damageCooldown";
    public static final String destroyed = "destroyed";
    public static final String invulnerable = "invulnerable";
    public static final String fireResistant = "fireResistant";
    public static final String inWater = "inWater";
    public static final String onFire = "onFire";
    public static final String moving = "moving";
    public static final String projectileDamage = "projectileDamage";
    public static final String pickupItems = "pickupItems";

    //-------------------------------------------------------------------
}