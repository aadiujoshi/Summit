/*
* BPA project by Aadi Joshi, Aditya Bhattaharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.entity.projectile;

import summit.game.GameUpdateEvent;
import summit.game.entity.Entity;
import summit.game.structure.Structure;
import summit.gfx.Light;
import summit.gfx.PaintEvent;
import summit.gfx.Renderer;
import summit.util.GameRegion;
import summit.util.Region;
import summit.util.Time;

public class Projectile extends Entity {

    //starting positions
    private float sx;
    private float sy;

    public Projectile(float x, float y, float angle, float mag, float width, float height) {
        super(x, y, width, height);
        super.setDx(mag*(float)Math.cos(angle));
        super.setDy(mag*(float)Math.sin(angle));
        super.setLight(new Light(x, y, 0.25f, -100, -100, -100));
        super.setHealth(1);

        if(getDx() > getDy()){
            if(getDx() < 0)
                setRenderOp(Renderer.NO_OP);
            else 
                setRenderOp(Renderer.FLIP_X);
        } else {
            if(getDy() < 0)
                setRenderOp(Renderer.ROTATE_90 | Renderer.FLIP_Y);
            else 
                setRenderOp(Renderer.ROTATE_90);
        }
    }

    @Override
    public void update(GameUpdateEvent e){
        if(Region.distance(sx, getX(), sy, getY()) >= 20){
            setDestroyed(true);
            return;
        }

        float delta_t = (float)e.getDeltaTimeNS()/Time.NS_IN_S;

        float nx = getX()+getDx()*delta_t;
        float ny = getY()+getDy()*delta_t;

        if(moveTo(e.getMap(), nx, ny)){
            setPos(nx, ny);
        } else {
            setDestroyed(true);
            return;
        }

        GameRegion contact = e.getMap().getContact(this);

        if(contact != null){
            if(contact instanceof Entity){
                collide((Entity)contact);
            } else if(contact instanceof Structure){
                ((Structure)contact).collide(this);
            }
            this.setDestroyed(true);
        }
    }
    
    @Override
    public void collide(Entity e) {
        e.damage(this);
        this.setDestroyed(true);
    }

    @Override
    public void gameClick(GameUpdateEvent e){

    }
}
