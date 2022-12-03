/*
* BPA project by Aadi Joshi, Aditya Bhattaharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.entity.projectile;

import summit.game.GameUpdateEvent;
import summit.game.animation.ScheduledEvent;
import summit.game.animation.Scheduler;
import summit.game.entity.Entity;
import summit.game.structure.Structure;
import summit.gfx.Light;
import summit.gfx.Renderer;
import summit.util.GameRegion;
import summit.util.Region;
import summit.util.Time;

public class Projectile extends Entity {

    //starting positions
    private float sx;
    private float sy;

    private GameRegion origin;

    private boolean enabled;

    public Projectile(GameRegion origin, float angle, float mag, float width, float height) {
        super(origin.getX(), origin.getY(), width, height);

        super.setDx(mag*(float)Math.cos(angle));
        super.setDy(mag*(float)Math.sin(angle));
        
        super.setLight(new Light(getX(), getY(), 0.25f, -100, -100, -100));
        super.setHealth(1);

        this.sx = getX();
        this.sy = getY();

        Scheduler.registerEvent(new ScheduledEvent(100, 1) {
            @Override
            public void run() {
                enabled = true;
            }
        });

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
        if(Region.distance(getX(), getY(), sx, sy) >= 20){
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
    }

    @Override
    public void collide(Entity contact) {
        if(!enabled && contact != ((Entity)origin)) return;

        contact.damage(this);
        this.setDestroyed(true);
    }

    @Override
    public void gameClick(GameUpdateEvent e){

    }
}
