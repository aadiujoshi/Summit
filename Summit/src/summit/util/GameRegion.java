package summit.util;

import summit.game.GameClickReciever;
import summit.game.entity.Entity;
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
public abstract class GameRegion extends Region implements Paintable, GameClickReciever{

    //optional for some classes
    private String sprite;

    private float spriteOffsetX;
    private float spriteOffsetY;
    
    private int rLayer = RenderLayers.TILE_LAYER;

    private int renderOp;
    private ColorFilter filter = ColorFilter.NOFILTER;
    private Light light = Light.NO_LIGHT;

    public GameRegion(float x, float y, float width, float height) {
        super(x, y, width, height);
    }
    
    public abstract void collide(Entity g);

    @Override
    public void setRenderLayer(OrderPaintEvent e){
        e.addToLayer(rLayer, this);

        if(light != Light.NO_LIGHT)
            light.setRenderLayer(e);
    }

    @Override
    public void paint(PaintEvent e) {
        this.outline(e);
        e.getRenderer().renderGame(sprite, 
                                    getX()+spriteOffsetX, getY()+spriteOffsetY, renderOp,
                                    filter,
                                    e.getCamera());
    }

    /**
     * Outline sprite if mouse is hovering
     * @param PaintEvent e
     */
    public void outline(PaintEvent e){
        if(this.contains(e.gameX(), e.gameY()))
            setRenderOp(renderOp | Renderer.OUTLINE_BLUE | Renderer.OUTLINE_GREEN | Renderer.OUTLINE_RED);
        else
            setRenderOp(renderOp & ~Renderer.OUTLINE_BLUE & ~Renderer.OUTLINE_GREEN & ~Renderer.OUTLINE_RED);
    }

    //-------- getters and setters  --------------------------------

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

}
