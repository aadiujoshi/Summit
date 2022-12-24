/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.tile;

import summit.game.GameUpdateEvent;
import summit.game.animation.ParticleAnimation;
import summit.game.entity.Entity;
import summit.gfx.ColorFilter;
import summit.gfx.Light;
import summit.gfx.OrderPaintEvent;
import summit.gfx.PaintEvent;
import summit.gfx.RenderLayers;
import summit.util.GameRegion;
import summit.util.ScheduledEvent;
import summit.util.GameScheduler;
import summit.util.GraphicsScheduler;

public abstract class Tile extends GameRegion {

    public static final int UNBREAKABLE = -1;

    //if true -> entities cannot pass through this tile
    private boolean boundary;
    // private boolean breakable;
    private boolean destroyed;
    
    private boolean iced;
    private int hitsToBreak;
    private int hits;
    private ColorFilter iceFilter;

    //managed by TileStack;
    //If not null, the tile is pushed to the top of the tile stack
    // private Tile reqPushTile;

    //Rendering hint, nothing to do with gameplay
    private int depth;

    private ScheduledEvent rotateAnim;

    //shut up its necessary
    private boolean animateParticles = false;
    private ParticleAnimation particleAnimation;

    public Tile(float x, float y, float width, float height){
        super(x, y, width, height);
        super.setRenderLayer(RenderLayers.TILE_LAYER);
        super.setOutline(true);
        super.setRenderOp((int)(Math.random()*5));

        this.hitsToBreak = UNBREAKABLE;
        this.iceFilter = new ColorFilter(0, 0, 50);
    }

    public Tile(float x, float y){
        this(x, y, 1, 1);
    }    

    @Override
    public void update(GameUpdateEvent e){
        //cover with snow
        // if(Math.random() < 0.000005)
        //     reqPushTile = new SnowTile(getX(), getY());
    }

    @Override
    public void paint(PaintEvent e){
        if(iced)
            setColorFilter(iceFilter);
        else{
            //reset to default filter
            setColorFilter(getColorFilter());
        }

        super.paint(e);

        // Point p = Renderer.toPixel(getX(), getY(), e.getCamera());

        // e.getRenderer().renderText((int)getX()+"", (int)p.x, (int)p.y-4, Renderer.NO_OP, null);
        // e.getRenderer().renderText((int)getY()+"", (int)p.x, (int)p.y+4, Renderer.NO_OP, null);
    }
    
    @Override
    public void setRenderLayer(OrderPaintEvent e){
        super.setRenderLayer(e); 

        if(particleAnimation != null)
            particleAnimation.setRenderLayer(e);

        if(particleAnimation != null && particleAnimation.shouldTerminate())
            particleAnimation = null;
    }

    @Override
    public void reinit(){
        rotateAnimation(rotateAnim != null);
    }

    //make breaking particle animation
    public void destroy(GameUpdateEvent e){
        e.getMap().addAnimation(new ParticleAnimation(getX(), getY(), 500, 15, getColor()));
    }

    @Override
    public void collide(Entity e) {
        e.set(Entity.inWater, false);
        e.set(Entity.onFire, false);

        if(e.is(Entity.moving) && animateParticles && particleAnimation == null){
            particleAnimation = new ParticleAnimation(e.getX(), e.getY(), 500, 15, getColor());
        }
    }

    @Override
    public void gameClick(GameUpdateEvent e){
        hits++;

        if(isBreakable()){
            if((iced && hits == 3) ||
                (!iced))
            this.setDestroy(true);
        }
    }

    @Override
    public String toString(){
        return getName() + "  " + getX() + "  " + getY() + "  " + getColorFilter().toString(); 
    }

    //-------------  getters and setters  ----------------------------------------------------------
    public boolean isBoundary() {
        return this.boundary;
    }

    public void setBoundary(boolean bounded) {
        this.boundary = bounded;
    }

    public boolean destroyed() {
        return this.destroyed;
    }

    public void setDestroy(boolean destroy) {
		this.destroyed = destroy;
	}

    @Override
    public void setLight(Light light) {
        light.setX(this.getX());
        light.setY(this.getY());
        super.setLight(light);
    }

    public boolean isBreakable() {
        return hitsToBreak != UNBREAKABLE;
    }

    public void setBreakable(int hitsToBreak) {
		this.hitsToBreak = hitsToBreak;
	}

    public int getDepth() {
        return this.depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void particleAnimation(boolean a){
        this.animateParticles = a;
    }
    
    public void rotateAnimation(boolean a){
        if(a){
            rotateAnim = new ScheduledEvent(300, ScheduledEvent.FOREVER) {
                @Override
                public void run() {
                    setRenderOp((int)(Math.random()*5));
                }
            };
            GraphicsScheduler.registerEvent(rotateAnim);
        } else {
            rotateAnim = null;
        }
    }
    
    public boolean isIced() {
        return this.iced;
    }

    public void setIced(boolean iced) {
        this.iced = iced;
        if(iced)
            hits = 0;
    }
    
    public void setIceFilter(ColorFilter iceFilter) {
        this.iceFilter = iceFilter;
    }

    // public Tile getReqPushTile() {
    //     return this.reqPushTile;
    // }

    // public void setReqPushTile(Tile reqPushTile) {
    //     this.reqPushTile = reqPushTile;
    // }
}