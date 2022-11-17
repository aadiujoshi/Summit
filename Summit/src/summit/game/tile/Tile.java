package summit.game.tile;

import summit.game.GameUpdateReciever;
import summit.gfx.ColorFilter;
import summit.gfx.Light;
import summit.gfx.OrderPaintEvent;
import summit.gfx.PaintEvent;
import summit.gfx.Paintable;
import summit.gfx.RenderLayers;
import summit.gui.GameClickReciever;
import summit.util.Region;

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
    public void renderLayer(OrderPaintEvent r){
        r.getRenderLayers().addToLayer(RenderLayers.TILE_LAYER, this);
        if(light != null)
            light.renderLayer(r);
    }

    @Override
    public void paint(PaintEvent e){
        if(sprite != null)
            e.getRenderer().renderGame(sprite, getX(), getY(), getRenderOp(), getColorFilter(), e.getCamera());
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