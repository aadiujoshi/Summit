package summit.game.entity;

import summit.game.GameMap;
import summit.game.GameUpdateEvent;
import summit.game.GameUpdateReciever;
import summit.gfx.PaintEvent;
import summit.gfx.Paintable;
import summit.gfx.Renderer;
import summit.gui.GameClickReciever;
import summit.util.Region;

public abstract class Entity extends Region implements Paintable, GameClickReciever, GameUpdateReciever{

    private float dx, dy;

    private float lastX, lastY;
    private boolean moving;
    
    //just metadata for class name
    private final String NAME = getClass().getSimpleName();

    private boolean inWater;
    private boolean destroyed;
    private boolean onFire;
    private float health;

    public Entity(float x, float y, float width, float height){
        super(x,y,width,height);

        inWater = false;
        destroyed = false;
    }

    @Override
    public void update(GameUpdateEvent e){
        // System.out.println(getX() + "  " + getY());
        if(getX() < -0.5 || getY() < -0.5) {
            return;
        }

        if(getHealth() <= 0){
            destroyed = true;
            destroy(e);
        }

        if(lastX != getX() || lastY != getY())
            moving = true;
        else 
            moving = false;
        lastX = getX();
        lastY = getY();

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
}