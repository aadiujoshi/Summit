package summit.game.entity;

import summit.game.GameClickReciever;
import summit.game.GameMap;
import summit.game.GameUpdateEvent;
import summit.game.GameUpdateReciever;
import summit.gfx.ColorFilter;
import summit.gfx.Light;
import summit.gfx.OrderPaintEvent;
import summit.gfx.PaintEvent;
import summit.gfx.Paintable;
import summit.gfx.RenderLayers;
import summit.util.Region;

public abstract class Entity extends Region implements Paintable, GameClickReciever, GameUpdateReciever{

    private float dx, dy;

    private int spriteOffsetX;
    private int spriteOffsetY;

    private float lastX, lastY;
    
    private int renderOp;
    private ColorFilter filter = new ColorFilter(0, 0, 0);
    private Light shadow;
    private Light light;

    //just metadata for class name
    private final String NAME = getClass().getSimpleName();

    private String onTile;

    private float maxHealth;
    private float health;

    private boolean invulnerable;
    private boolean inWater;
    private boolean destroyed;
    private boolean onFire;
    private boolean moving;

    public Entity(float x, float y, float width, float height){
        super(x,y,width,height);

        Light sdw = new Light(x, y, 1f, -30, -30, -30);
        sdw.setRenderLayer(RenderLayers.STRUCTURE_ENTITY_LAYER-1);
        this.shadow = sdw;

        this.light = Light.NO_LIGHT;
    }

    @Override
    public void setRenderLayer(OrderPaintEvent ope){
        ope.addToLayer(RenderLayers.STRUCTURE_ENTITY_LAYER, this);
        light.setRenderLayer(ope);
        shadow.setRenderLayer(ope);
    }

    @Override
    public void paint(PaintEvent e){
        e.getRenderer().renderLight(light, e.getCamera());
        e.getRenderer().renderLight(shadow, e.getCamera());
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

        onTile = e.getMap().getTileAt(getX(), getY()).peekTile().getName();

        if(onTile.equals("WaterTile")){
            setInWater(true);
        } else { 
            setInWater(false); 
        }
            
    }

    abstract public void damage(GameUpdateEvent ge, Entity e);
    abstract public void destroy(GameUpdateEvent ge);
    abstract public void collide(Entity e);

    public boolean moveTo(GameMap map, float newX, float newY){
        if(map.getTileAt(newX, newY).peekTile().isBoundary() || 
            newX <= -0.5 || newX > map.getWidth() || 
            newY <= -0.5 || newY > map.getHeight()){
            return false;
        }
        return true;
    }

    protected void updateIsMoving(){
        if(lastX != getX() || lastY != getY()){
            moving = true;
            lastX = getX();
            lastY = getY();
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
        if(light != null){
            light.setX(x);
        }
        if(shadow != null){
            shadow.setX(x);
        }
    }

    @Override 
    public void setY(float y){
        super.setY(y);
        if(light != null){
            light.setY(y);
        }
        if(shadow != null){
            shadow.setY(y);
        }
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
    
    public int getSpriteOffsetX() {
        return this.spriteOffsetX;
    }

    public void setSpriteOffsetX(int spriteOffsetX) {
        this.spriteOffsetX = spriteOffsetX;
    }

    public int getSpriteOffsetY() {
        return this.spriteOffsetY;
    }

    public void setSpriteOffsetY(int spriteOffsetY) {
        this.spriteOffsetY = spriteOffsetY;
    }

    public String getOnTile(){
        return this.onTile;
    }
    
    public boolean isInvulnerable() {
        return this.invulnerable;
    }

    public void setInvulnerable(boolean invulnerable) {
        this.invulnerable = invulnerable;
    }
}