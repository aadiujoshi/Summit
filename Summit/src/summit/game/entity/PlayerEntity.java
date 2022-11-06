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
    }

    @Override
    public void paint(PaintEvent e) {
        
        e.getRenderer().renderGame( (Time.timeMs()-lastAnimationChange > 250) ? Sprite.PLAYER_FACE_BACK_1 : Sprite.PLAYER_FACE_BACK_2, 
                                    getX(), getY(), Renderer.FLIP_NONE, e.getCamera());

        if((Time.timeMs()-lastAnimationChange > 500))
            lastAnimationChange = Time.timeMs();
    }

    @Override
    public void gameClick(GameMap map, MouseEvent e) {
        
    }

    @Override
    public void update(GameUpdateEvent e) {
        if(Controls.W){
            camera.setY(this.getY()+0.0075f);
            this.setY(this.getY()+0.0075f);
        }
        if(Controls.A){
            camera.setX(getX()-0.0075f);
            this.setX(this.getX()-0.0075f);
        }
        if(Controls.S){
            camera.setY(getY()-0.0075f);
            this.setY(this.getY()-0.0075f);
        }
        if(Controls.D){
            camera.setX(getX()+0.0075f);
            this.setX(this.getX()+0.0075f);
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
