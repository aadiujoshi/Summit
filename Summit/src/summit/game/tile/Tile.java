package summit.game.tile;

import summit.game.GameClickReciever;
import summit.game.GameMap;
import summit.game.GameUpdateEvent;
import summit.game.GameUpdateReciever;
import summit.game.animation.ScheduledEvent;
import summit.game.entity.Entity;
import summit.gfx.ColorFilter;
import summit.gfx.Light;
import summit.gfx.OrderPaintEvent;
import summit.gfx.PaintEvent;
import summit.gfx.Paintable;
import summit.gfx.RenderLayers;
import summit.gfx.Renderer;
import summit.util.GameRegion;
import summit.util.Region;

import java.awt.Point;
import java.awt.geom.Point2D.Float;

public abstract class Tile extends GameRegion implements GameUpdateReciever {

    //if true -> entities cannot pass through this tile
    private boolean boundary;
    private boolean breakable = false;
    private boolean destroyed = false;

    //Rendering hint, nothing to do with gameplay
    private boolean raisedTile = true;
    private ScheduledEvent animation;

    private final String NAME = getClass().getSimpleName();

    public Tile(float x, float y, float width, float height){
        super(x, y, width, height);
        super.setRLayer(RenderLayers.TILE_LAYER);
    }

    public Tile(float x, float y){
        this(x, y, 1, 1);
    }    

    @Override
    public void paint(PaintEvent e){
        super.paint(e);

        if(false){
            GameMap map = e.getLoadedMap();

            String u = map.getTileAt(getX(), getY()+1).getName();
            String d = map.getTileAt(getX(), getY()-1).getName();
            String l = map.getTileAt(getX()-1, getY()).getName();
            String r = map.getTileAt(getX(), getY()+1).getName();

            Point p = Renderer.toPixel(getX(), getY(), e.getCamera());

            if(!u.equals("SnowTile")){
                e.getRenderer().filterRect(p.x-7, p.y-5, 16, 3, new ColorFilter(-30, -30, -30));
            } 
            if(!d.equals("SnowTile")){
                e.getRenderer().filterRect(p.x-8, p.y-5, 16, 3, new ColorFilter(-30, -30, -30));
            }
            if(!l.equals("SnowTile")){
                e.getRenderer().filterRect(p.x-7, p.y-8, 3, 16, new ColorFilter(-30, -30, -30));
            }
            if(!r.equals("SnowTile")){
                e.getRenderer().filterRect(p.x+8, p.y+5, 3, 16, new ColorFilter(-30, -30, -30));
            }
        }
    }

    @Override
    public void collide(Entity e) {
        e.setInWater(false);
        e.setOnFire(false);
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

    public void animate(boolean a){
        if(a){
            animation = new ScheduledEvent(300, ScheduledEvent.FOREVER) {
                @Override
                public void run() {
                    setRenderOp((int)(Math.random()*5));
                }
            };
        } else {
            animation = null;
        }
    }
}