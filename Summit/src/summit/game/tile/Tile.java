package summit.game.tile;

import summit.game.GameClickReciever;
import summit.game.GameUpdateEvent;
import summit.game.GameUpdateReciever;
import summit.gfx.ColorFilter;
import summit.gfx.Light;
import summit.gfx.OrderPaintEvent;
import summit.gfx.PaintEvent;
import summit.gfx.Paintable;
import summit.gfx.RenderLayers;
import summit.gfx.Renderer;
import summit.util.Region;
import java.awt.geom.Point2D.Float;

public abstract class Tile extends Region implements GameClickReciever, Paintable, GameUpdateReciever {

    //if true -> entitys cannot pass through this tile
    private boolean boundary;

    private String sprite;
    
    //random tile rotation
    private int renderOp = (int)(Math.random()*5);
    private ColorFilter filter = new ColorFilter(0, 0, 0);
    private Light light;

    private boolean destroy = false;

    private final String NAME = getClass().getSimpleName();

    public Tile(float x, float y, float width, float height){
        super(x, y, width, height);
    }

    public Tile(float x, float y){
        this(x, y, 1, 1);
    }
    
    @Override
    public void update(GameUpdateEvent e){
        if(!e.validUpdate())
            return;
        
        java.awt.geom.Point2D.Float mt = Renderer.toTile(e.mouseX(), e.mouseY(), e.getMap().getPlayer().getCamera());

        if(this.contains(mt.x, mt.y))
            setRenderOp(getRenderOp() | Renderer.OUTLINE_BLUE | Renderer.OUTLINE_GREEN | Renderer.OUTLINE_RED);
        else
            setRenderOp(getRenderOp() & ~Renderer.OUTLINE_BLUE & ~Renderer.OUTLINE_GREEN & ~Renderer.OUTLINE_RED);
    }

    @Override
    public void setRenderLayer(OrderPaintEvent r){
        r.getRenderLayers().addToLayer(RenderLayers.TILE_LAYER, this);
        if(light != null)
            light.setRenderLayer(r);
    }

    @Override
    public void paint(PaintEvent e){
        
        // if((renderOp & Renderer.OUTLINE_RED) == Renderer.OUTLINE_RED)
        //     System.out.println(Integer.toBinaryString(renderOp));
        
        if(sprite != null)
            e.getRenderer().renderGame(sprite, getX(), getY(), renderOp, filter, e.getCamera());
        java.awt.geom.Point2D.Float p = Renderer.toPixel(getX(), getY(), e.getCamera());
    } 


    //-------------  getters and setters  ----------------------------------------------------------

    public String getSprite() {
        return this.sprite;
    }

    public void setSprite(String sprite) {
        this.sprite = sprite;
    }

    public boolean isBoundary() {
        return this.boundary;
    }

    public void setBoundary(boolean bounded) {
        this.boundary = bounded;
    }

    public int getRenderOp() {
        return this.renderOp;
    }

    public void setRenderOp(int renderOp) {
		this.renderOp = renderOp;
    }

    public boolean destroyed() {
        return this.destroy;
    }

    public void setDestroy(boolean destroy) {
		this.destroy = destroy;
	}
    
    public String getName(){
        return this.NAME;
    }
    
    public ColorFilter getColorFilter() {
        return this.filter;
    }

    public void setColorFilter(ColorFilter filter) {
        this.filter = filter;
    }

    public Light getLight() {
        return this.light;
    }

    public void setLight(Light light) {
        this.light = light;
    }
}