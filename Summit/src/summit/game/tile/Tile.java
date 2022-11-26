package summit.game.tile;

import summit.game.GameUpdateEvent;
import summit.game.GameUpdateReciever;
import summit.game.animation.ParticleAnimation;
import summit.game.animation.ScheduledEvent;
import summit.game.animation.Scheduler;
import summit.game.entity.Entity;
import summit.gfx.Light;
import summit.gfx.OrderPaintEvent;
import summit.gfx.PaintEvent;
import summit.gfx.RenderLayers;
import summit.gfx.Renderer;
import summit.util.GameRegion;

public abstract class Tile extends GameRegion implements GameUpdateReciever {

    //if true -> entities cannot pass through this tile
    private boolean boundary;
    private boolean breakable = false;
    private boolean destroyed = false;

    //Rendering hint, nothing to do with gameplay
    private boolean raisedTile = true;

    private ScheduledEvent rotateAnim;

    //shut up its necessary
    private boolean animateParticles = false;
    private ParticleAnimation particleAnim;

    private final String NAME = getClass().getSimpleName();

    public Tile(float x, float y, float width, float height){
        super(x, y, width, height);
        super.setRLayer(RenderLayers.TILE_LAYER);
    }

    public Tile(float x, float y){
        this(x, y, 1, 1);
    }    

    @Override
    public void setRenderLayer(OrderPaintEvent e){
        super.setRenderLayer(e); 

        if(particleAnim != null)
            particleAnim.setRenderLayer(e);

        if(particleAnim != null && particleAnim.terminate())
            particleAnim = null;
    }

    //make breaking particle animation
    public void destroy(GameUpdateEvent e){
        e.getMap();
    }

    @Override
    public void paint(PaintEvent e){
        super.paint(e);
    }

    @Override
    public void collide(Entity e) {
        e.setInWater(false);
        e.setOnFire(false);

        if(e.isMoving() && animateParticles && particleAnim == null){
            particleAnim = new ParticleAnimation(e.getX(), e.getY(), 500, 15, getColor());
        }
    }

    @Override
    public void gameClick(GameUpdateEvent e){
        if(breakable){
            this.setDestroy(true);
            System.out.println(getX() + "  " + getY());
        }
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
    
    public String getName(){
        return this.NAME;
    }

    @Override
    public void setLight(Light light) {
        light.setX(this.getX());
        light.setY(this.getY());
        super.setLight(light);
    }

    public boolean isBreakable() {
        return this.breakable;
    }

    public void setBreakable(boolean breakable) {
		this.breakable = breakable;
	}

    public boolean isRaisedTile() {
        return this.raisedTile;
    }

    public void setRaisedTile(boolean raisedTile) {
		this.raisedTile = raisedTile;
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
            Scheduler.registerEvent(rotateAnim);
        } else {
            rotateAnim = null;
        }
    }
}