package summit.game.entity;

import summit.game.GameMap;
import summit.game.GameUpdateEvent;
import summit.game.GameUpdateReciever;
import summit.gfx.ColorFilter;
import summit.gfx.Light;
import summit.gfx.Paintable;
import summit.gui.GameClickReciever;
import summit.util.Region;

public abstract class Entity extends Region implements Paintable, GameClickReciever, GameUpdateReciever{

    private float dx, dy;

    private float lastX, lastY;
    
    private int renderOp;
    private ColorFilter filter;
    private Light light;

    //just metadata for class name
    private final String NAME = getClass().getSimpleName();

    private float maxHealth;
    private float health;

    private boolean inWater;
    private boolean destroyed;
    private boolean onFire;
    private boolean moving;

    public Entity(float x, float y, float width, float height){
        super(x,y,width,height);
    }

    @Override
    public void update(GameUpdateEvent e){
        if(getX() < -0.5 || getY() < -0.5) {
            return;
        }
        
        if(getHealth() <= 0){
            destroyed = true;
            destroy(e);
        }

        // System.out.println(getX() + "  " + getY() + "  " + lastX + "  " + lastY);
        if(lastX != getX() || lastY != getY()){
            moving = true;
            lastX = getX();
            lastY = getY();
        } else {
            moving = false;
        }

        if(e.getMap().getTileAt(getX(), getY()).peekTile().getName().equals("WaterTile")){
            setInWater(true);
        } else { setInWater(false); }
            
    }

    abstract public void damage(GameUpdateEvent ge, Entity e);
    abstract public void destroy(GameUpdateEvent ge);
    abstract public void collide(Entity e);

    public boolean moveTo(GameMap map, float newX, float newY){
        if(map.getTileAt(newX, newY).peekTile().isBoundary()){
            return false;
        }
        return true;
    }

    //---------------------------------------------------------
    //getters and setters
    //---------------------------------------------------------

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
    
    public int getRenderOp() {
        return this.renderOp;
    }

    public void setRenderOp(int renderOp) {
        this.renderOp = renderOp;
    }
    
    public float getMaxHealth() {
        return this.maxHealth;
    }

    public void setMaxHealth(float maxHealth) {
        this.maxHealth = maxHealth;
    }
    
    public ColorFilter getColorFilter() {
        return this.filter;
    }

    public void setColorFilter(ColorFilter cFilter) {
        this.filter = cFilter;
    }
    
    public Light getLight() {
        return this.light;
    }

    public void setLight(Light light) {
        this.light = light;
    }
}