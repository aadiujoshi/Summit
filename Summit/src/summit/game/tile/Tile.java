/*
* BPA project by Aadi Joshi, Aditya Bhattaharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.tile;

import java.awt.Point;

import summit.game.GameMap;
import summit.game.GameUpdateEvent;
import summit.game.GameUpdateReciever;
import summit.game.animation.ParticleAnimation;
import summit.game.animation.ScheduledEvent;
import summit.game.animation.Scheduler;
import summit.game.entity.Entity;
import summit.gfx.ColorFilter;
import summit.gfx.Light;
import summit.gfx.OrderPaintEvent;
import summit.gfx.PaintEvent;
import summit.gfx.RenderLayers;
import summit.gfx.Renderer;
import summit.util.GameRegion;

public abstract class Tile extends GameRegion implements GameUpdateReciever {

    //if true -> entities cannot pass through this tile
    private boolean boundary;
    private boolean breakable;
    private boolean destroyed;

    //managed by TileStack;
    //If not null, the tile is pushed to the top of the tile stack
    private Tile reqPushTile;

    //Rendering hint, nothing to do with gameplay
    private int depth;

    private ScheduledEvent rotateAnim;

    //shut up its necessary
    private boolean animateParticles = false;
    private ParticleAnimation particleAnimation;

    public Tile(float x, float y, float width, float height){
        super(x, y, width, height);
        super.setRLayer(RenderLayers.TILE_LAYER);
        super.setOutline(true);
    }

    public Tile(float x, float y){
        this(x, y, 1, 1);
    }    

    @Override
    public void paint(PaintEvent e){
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

        if(particleAnimation != null && particleAnimation.terminate())
            particleAnimation = null;
    }

    //make breaking particle animation
    public void destroy(GameUpdateEvent e){
        e.getMap().addParticleAnimation(new ParticleAnimation(getX(), getY(), 500, 15, getColor()));
    }

    @Override
    public void collide(Entity e) {
        e.setInWater(false);
        e.setOnFire(false);

        if(e.isMoving() && animateParticles && particleAnimation == null){
            particleAnimation = new ParticleAnimation(e.getX(), e.getY(), 500, 15, getColor());
        }
    }

    @Override
    public void gameClick(GameUpdateEvent e){
        System.out.println(this);
        if(breakable){
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
        return this.breakable;
    }

    public void setBreakable(boolean breakable) {
		this.breakable = breakable;
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
            Scheduler.registerEvent(rotateAnim);
        } else {
            rotateAnim = null;
        }
    }
    
    public Tile getReqPushTile() {
        return this.reqPushTile;
    }

    public void setReqPushTile(Tile reqPushTile) {
        this.reqPushTile = reqPushTile;
    }
}