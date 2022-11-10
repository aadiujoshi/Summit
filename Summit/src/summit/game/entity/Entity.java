package summit.game.entity;

import summit.game.GameUpdateEvent;
import summit.game.GameUpdateReciever;
import summit.gfx.PaintEvent;
import summit.gfx.Paintable;
import summit.gfx.Renderer;
import summit.gui.GameClickReciever;
import summit.util.Region;

public abstract class Entity extends Region implements Paintable, GameClickReciever, GameUpdateReciever{

    private float dx, dy;

    private float lastX, lastY;
    private boolean moving;
    
    public Entity(float x, float y, float width, float height){
        super(x,y,width,height);
    }

    @Override
    public void update(GameUpdateEvent e){
        if(lastX != getX() && lastY != getY())
            moving = true;
        lastX = getX();
        lastY = getY();
    }

    abstract public void damage(GameUpdateEvent ge, Entity e);
    abstract public void destroy(GameUpdateEvent ge);
    abstract public void collide(Entity e);

    //---------------------------------------------------------
    //getters and setters
    //---------------------------------------------------------

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
}