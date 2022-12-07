/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.entity;

import summit.game.GameMap;
import summit.game.GameUpdateEvent;
import summit.game.animation.ParticleAnimation;
import summit.game.entity.projectile.Projectile;
import summit.gfx.ColorFilter;
import summit.gfx.Light;
import summit.gfx.OrderPaintEvent;
import summit.gfx.PaintEvent;
import summit.gfx.RenderLayers;
import summit.util.Direction;
import summit.util.GameRegion;
import summit.util.ScheduledEvent;
import summit.util.Scheduler;

public abstract class Entity extends GameRegion{

    private float dx, dy;

    //knockback lasts for 2 seconds
    private Knockback kb;

    //last x and last y; used to check if entity is moving
    private float lx, ly;

    //facing of the entity
    private Direction facing;

    private Light shadow;

    private float hitDamage;

    //coefficient
    private float damageResistance;

    private float maxHealth;
    private float health;

    public Entity(float x, float y, float width, float height){
        super(x,y,width,height);
        super.setRLayer(RenderLayers.STRUCTURE_ENTITY_LAYER);

        this.maxHealth = 1;
        this.health = maxHealth;
        this.hitDamage = 0;
        this.damageResistance = 0;

        this.lx = x;
        this.ly = y;
        
        this.shadow = new Light(x, y, 1f, -50, -50, -50);

        this.add(hitCooldown);
        this.add(damageCooldown);
        this.add(destroyed);
        this.add(invulnerable);
        this.add(fireResistant);
        this.add(inWater);
        this.add(onFire);
        this.add(moving);
        this.add(projectileDamage);

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
        if(health <= 0){
            set(destroyed, true);
            return;
        }

        if(kb != null){
            kb.move(e);
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
        if(hitBy.is(hitCooldown))
            return;
        if(hitBy instanceof Projectile && !is(projectileDamage))
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

        set(hitCooldown, true);
        set(damageCooldown, true);
        
        if(health <= 0)
            set(destroyed, true);
    }

    public void destroy(GameUpdateEvent e){
        e.getMap().addParticleAnimation(
                new ParticleAnimation(getX(), getY(), 
                                        500, 
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

    private void updateMoving(){
        if(lx != getX() || ly != getY()){
            this.set(moving, true);
        } else {
            this.set(moving, false);
        }

        if(getY() > ly){
            facing = Direction.NORTH;
        }
        if(getY() < ly){
            facing = Direction.SOUTH;
        }
        if(getX() > lx){
            facing =  Direction.EAST;
        }
        if(getX() < lx){
            facing = Direction.WEST;
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
            case hitCooldown -> {
                if(is(hitCooldown)){
                    Scheduler.registerEvent(new ScheduledEvent(500,1) {
                        @Override
                        public void run() {
                            set(hitCooldown, false);
                        }
                    });
                }

                break;
            }

            case damageCooldown -> {
                if(is(damageCooldown)){
                    Scheduler.registerEvent(new ScheduledEvent(500,1) {
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

    public void changeHealth(float c){
        this.health += c;
    }
    
    public float getMaxHealth() {
        return this.maxHealth;
    }

    public void setMaxHealth(float maxHealth) {
        this.maxHealth = maxHealth;
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


    
    //-----------  game tag / property keys ------------------------------

    public static final String hitCooldown = "hitCooldown";
    public static final String damageCooldown = "damageCooldown";
    public static final String destroyed = "destroyed";
    public static final String invulnerable = "invulnerable";
    public static final String fireResistant = "fireResistant";
    public static final String inWater = "inWater";
    public static final String onFire = "onFire";
    public static final String moving = "moving";
    public static final String projectileDamage = "projectileDamage";

    //-------------------------------------------------------------------
}