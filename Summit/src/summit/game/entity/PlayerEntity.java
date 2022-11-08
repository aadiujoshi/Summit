package summit.game.entity;

import java.awt.event.MouseEvent;

import summit.game.GameMap;
import summit.game.GameUpdateEvent;
import summit.gfx.Camera;
import summit.gfx.PaintEvent;
import summit.gfx.Renderer;
import summit.gfx.Sprite;
import summit.util.Controls;
import summit.util.Time;

public class PlayerEntity extends HumanoidEntity{

    private Camera camera;

    private long lastAnimationChange = Time.timeMs();

    public PlayerEntity(float x, float y) {
        super(x, y, 1, 1);
        super.setDx(2.5f);
        super.setDy(2.5f);
    }

    @Override
    public void paint(PaintEvent e) {
        
        e.getRenderer().renderGame(Sprite.PLAYER_FACE_BACK_1, 
                                    getX(), getY(), 
                                    (Time.timeMs()-lastAnimationChange > 250) ? Renderer.FLIP_NONE : Renderer.FLIP_X, 
                                    e.getCamera());

        if((Time.timeMs()-lastAnimationChange > 500))
            lastAnimationChange = Time.timeMs();
    }

    @Override
    public void gameClick(GameMap map, MouseEvent e) {
        
    }

    @Override
    public void update(GameUpdateEvent e) {

        

        float del_x = (getDx()/1000)*e.getDeltaTime();
        float del_y = (getDy()/1000)*e.getDeltaTime();

        if(Controls.W){
            camera.setY(this.getY()+del_y);
            this.setY(this.getY()+del_y);
        }
        if(Controls.A){
            camera.setX(getX()-del_x);
            this.setX(this.getX()-del_x);
        }
        if(Controls.S){
            camera.setY(getY()-del_y);
            this.setY(this.getY()-del_y);
        }
        if(Controls.D){
            camera.setX(getX()+del_x);
            this.setX(this.getX()+del_x);
        }
    }

    @Override
    public void damage(GameUpdateEvent ge, Entity e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void destroy(GameUpdateEvent ge) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void collide(Entity e) {
        // TODO Auto-generated method stub
        
    }
    
    //getters and setters

    
    public void setCamera(Camera camera) {
        this.camera = camera;
    }
}
