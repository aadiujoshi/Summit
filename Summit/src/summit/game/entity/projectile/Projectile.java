/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.entity.projectile;

import summit.game.GameUpdateEvent;
import summit.game.entity.Entity;
import summit.gfx.PaintEvent;
import summit.gfx.Renderer;
import summit.util.GameObject;
import summit.util.Region;
import summit.util.Time;

public class Projectile extends Entity {

    //starting positions
    private float sx;
    private float sy;

    private float contactDamage;

    private GameObject origin;
    
    public Projectile(GameObject origin, float angle, float speed, float width, float height) {
        super(origin.getX(), origin.getY(), width, height);

        super.setDx(speed*(float)Math.cos(angle));
        super.setDy(speed*(float)Math.sin(angle));
        
        // super.setLight(new Light(getX(), getY(), 0.25f, -100, -100, -100));

        super.setHealth(1);

        this.origin = origin;

        this.sx = getX();
        this.sy = getY();
        
        float deg = (float)Math.toDegrees(angle);

        if(deg < 0){
            deg = 360+deg;
        }

        //face east
        if((deg <= 45 && deg >= 0) || (deg <= 360 && deg >= 315)){
            setRenderOp(Renderer.FLIP_X);
        }
        //face west
        else if(deg <= 215 && deg >= 135){
            setRenderOp(Renderer.NO_OP);
        }
        //face north
        else if(deg < 135 && deg > 45){
            setRenderOp(Renderer.ROTATE_90);
        }
        //face south
        else if(deg < 315 && deg > 215){
            setRenderOp(Renderer.ROTATE_90 | Renderer.FLIP_Y);
        }
    }

    @Override
    public void attack(float targetX, float targetY, GameUpdateEvent ev) {
    }

    @Override
    public void paint(PaintEvent e){

        // System.out.println(getFacing());

        // setRenderOp(
        //     switch(getFacing()){
        //     case EAST -> Renderer.FLIP_X;
        //     case NORTH -> Renderer.ROTATE_90;
        //     case SOUTH -> Renderer.ROTATE_90 | Renderer.FLIP_Y;
        //     case WEST -> Renderer.NO_OP;
        //     case NW -> 0;
        //     case NE -> 0;
        //     case SW -> 0;
        //     case SE -> 0;
        // });

        super.paint(e);
    }

    @Override
    public void update(GameUpdateEvent e) throws Exception{
        if(Region.distance(getX(), getY(), sx, sy) >= 20){
            set(destroyed, true);
            return;
        }

        float delta_t = (float)e.getDeltaTimeNS()/Time.NS_IN_S;

        float nx = getX()+getDx()*delta_t;
        float ny = getY()+getDy()*delta_t;

        if(moveTo(e.getMap(), nx, ny)){
            setPos(nx, ny);
        } else {
            set(destroyed, true);
            return;
        }

        set(inWater, false);
    }

    @Override
    public void collide(GameUpdateEvent e, Entity contact) {
        if(((GameObject)contact) == origin) return;

        contact.damage(e, this);

        set(destroyed, true);
    }

    @Override
    public float getDx(){
        //change speed back to normal
        return super.getDx() * (is(inWater) ? 2 : 1);
    }

    @Override
    public float getDy(){
        //change speed back to normal
        return super.getDy() * (is(inWater) ? 2 : 1);
    }

    @Override
    public void gameClick(GameUpdateEvent e){

    }

    public GameObject getOrigin() {
        return this.origin;
    }
    
    @Override
    public float getAttackDamage() {
        return this.contactDamage;
    }

    public void setAttackDamage(float contactDamage) {
        this.contactDamage = contactDamage;
    }
}
