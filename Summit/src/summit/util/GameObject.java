package summit.util;

import java.util.HashMap;

import summit.game.GameClickReciever;
import summit.game.GameUpdateEvent;
import summit.game.GameUpdateReciever;
import summit.game.entity.Entity;
import summit.game.entity.mob.Player;
import summit.gfx.ColorFilter;
import summit.gfx.Light;
import summit.gfx.OrderPaintEvent;
import summit.gfx.PaintEvent;
import summit.gfx.Paintable;
import summit.gfx.RenderLayers;
import summit.gfx.Renderer;

/**
 * The superclass for basically anything in the actual game
 */
public abstract class GameObject extends Region implements Paintable, GameUpdateReciever, GameClickReciever{

    private String sprite;

    //render game distance values (offset 1 => 16 pixels)
    private float spriteOffsetX;
    private float spriteOffsetY;

    //outline if mouse is hovering over it
    private boolean outline = true;

    private boolean moveable = true;
    private boolean enabled = true;

    private HashMap<String, Boolean> tags;
    
    //the render layer at which this object is added to
    private int rLayer = RenderLayers.TILE_LAYER;

    private int renderOp;
    private ColorFilter filter = ColorFilter.NOFILTER;
    private Light light = Light.NO_LIGHT;

    private final String NAME = getClass().getSimpleName();

    //the general color for the region
    private int color = Renderer.toIntRGB(170, 214, 230);

    public GameObject(float x, float y, float width, float height) {
        super(x, y, width, height);
        
        this.tags = new HashMap<>();
    }

    public abstract void collide(GameUpdateEvent ev, Entity e);

    @Override
    public void setRenderLayer(OrderPaintEvent e){
        e.addToLayer(rLayer, this);

        if(light != Light.NO_LIGHT)
            light.setRenderLayer(e);
    }

    @Override
    public void paint(PaintEvent e) {
        paint0(e);
    }

    /**
     * Can be used by subclasses for basic rendering
     * @param e PaintEvent
     */
    public final void paint0(PaintEvent e){
        if(outline())
            this.outline(e);
        e.getRenderer().renderGame(getSprite(), 
                                getX()+getSpriteOffsetX(), getY()+getSpriteOffsetY(), getRenderOp(),
                                getColorFilter(),
                                e.getCamera());
    }

    /**
     * Outline sprite if mouse is hovering
     * 
     * @param e PaintEvent
     */
    public void outline(PaintEvent e){
        Player p = e.getLoadedMap().getPlayer();

        float dist = distance(p.getX(), p.getY(), getX(), getY());

        if(this.contains(e.gameX(), e.gameY()) && dist <= p.getReach())
            setRenderOp(renderOp | Renderer.OUTLINE_BLUE | Renderer.OUTLINE_GREEN | Renderer.OUTLINE_RED);
        else
            setRenderOp(renderOp & ~Renderer.OUTLINE_BLUE & ~Renderer.OUTLINE_GREEN & ~Renderer.OUTLINE_RED);
    }
    
    /**
     * Called on every GameRegion after a game save is deserialized (GameWorld is reopened)
     * </p>
     * Usually used to 
     */
    public void reinit(){
        // System.out.println(this);
    }

    //-------- getters and setters  --------------------------------

    @Override
    public String toString(){
        return NAME + " " + super.toString();
    }

    @Override
    public void setX(float x){
        super.setX(x);
        if(light != null){
            light.setX(x);
        }
    }

    @Override
    public void setY(float y){
        super.setY(y);
        if(light != null){
            light.setY(y);
        }
    }

    /**
     * is property
     */
    final public boolean is(String property){
        Object b = tags.get(property);
        return (b == null) ? false : (boolean)b;
    }

    /**
     * set property value
     */
    public void set(String property, boolean value){
        tags.put(property, value);
    }

    /**
     * puts a property and sets it to false
     */
    final public void add(String property){
        tags.put(property, false);
    }

    public float getSpriteOffsetX() {
        return this.spriteOffsetX;
    }

    public void setSpriteOffsetX(float spriteOffsetX) {
        this.spriteOffsetX = spriteOffsetX;
    }

    public float getSpriteOffsetY() {
        return this.spriteOffsetY;
    }

    public void setSpriteOffsetY(float spriteOffsetY) {
        this.spriteOffsetY = spriteOffsetY;
    }
    
    public void setSprite(String sprite){
        this.sprite = sprite;
    }

    public void setRenderOp(int r){
        renderOp = r;
    }

    public int getRenderOp(){
        return renderOp;
    }

    public void setColorFilter(ColorFilter cf){
        this.filter = cf;
    }

    public ColorFilter getColorFilter(){
        return this.filter;
    }
    
    public Light getLight() {
        return this.light;
    }

    public void setLight(Light light) {
        this.light = light;
    }

    public int getRLayer() {
        return this.rLayer;
    }

    public void setRenderLayer(int rLayer) {
        this.rLayer = rLayer;
    }
    
    public int getColor() {
        return this.color;
    }

    public void setColor(int color) {
        this.color = color;
    }
    
    public boolean outline() {
        return this.outline;
    }

    public void setOutline(boolean outline) {
        this.outline = outline;
    }
    
    public String getName(){
        return this.NAME;
    }

    public String getSprite(){
        return this.sprite;
    }
    
    public boolean isMoveable() {
        return this.moveable;
    }

    public void setMoveable(boolean valid) {
		this.moveable = valid;
	}

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
