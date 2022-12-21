package summit.util;

import java.util.HashMap;

import summit.game.GameClickReciever;
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
public abstract class GameRegion extends Region implements Paintable, GameUpdateReciever, GameClickReciever{

    //optional for some classes
    private String sprite;

    private float spriteOffsetX;
    private float spriteOffsetY;

    private boolean outline = true;

    private boolean valid = true;

    private HashMap<String, Boolean> tags;

    // +x is right -x is left +y is up -y is down
    //DO NOT USE
    private float xOffset;
    private float yOffset;
    
    private int rLayer = RenderLayers.TILE_LAYER;

    private int renderOp;
    private ColorFilter filter = ColorFilter.NOFILTER;
    private Light light = Light.NO_LIGHT;

    private final String NAME = getClass().getSimpleName();

    //the general color for the region
    private int color = Renderer.toIntRGB(170, 214, 230);

    public GameRegion(float x, float y, float width, float height) {
        super(x, y, width, height);

        this.tags = new HashMap<>();
    }

    public abstract void collide(Entity e);

    @Override
    public void setRenderLayer(OrderPaintEvent e){
        e.addToLayer(rLayer, this);

        if(light != Light.NO_LIGHT)
            light.setRenderLayer(e);
    }

    @Override
    public void paint(PaintEvent e) {
        if(outline())
            this.outline(e);
        e.getRenderer().renderGame(getSprite(), 
                                    getX()+getSpriteOffsetX(), getY()+getSpriteOffsetY(), getRenderOp(),
                                    getColorFilter(),
                                    e.getCamera());
    }

    /**
     * Outline sprite if mouse is hovering
     */
    public void outline(PaintEvent e){
        Player p = e.getLoadedMap().getPlayer();

        float dist = distance(p.getX(), p.getY(), getX(), getY());

        if(this.contains(e.gameX(), e.gameY()) && dist <= p.getAttackRange())
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
        
    }

    //-------- getters and setters  --------------------------------

    @Override
    public String toString(){
        return NAME + ": " + super.toString();
    }

    @Override
    public void setX(float x){
        super.setX(x + xOffset);
        if(light != null){
            light.setX(x + yOffset);
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

    public void setRLayer(int rLayer) {
        this.rLayer = rLayer;
    }

    public float getXOffset() {
        return this.xOffset;
    }

    public void setXOffset(float xOffset) {
        this.xOffset = xOffset;
    }

    public float getYOffset() {
        return this.yOffset;
    }

    public void setYOffset(float yOffset) {
        this.yOffset = yOffset;
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
    
    public boolean isValid() {
        return this.valid;
    }

    public void setValid(boolean valid) {
		this.valid = valid;
	}
}
