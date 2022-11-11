package summit.game.entity;

import java.awt.event.MouseEvent;

import summit.game.GameMap;
import summit.game.GameUpdateEvent;
import summit.gfx.Camera;
import summit.gfx.PaintEvent;
import summit.gfx.Renderer;
import summit.gfx.Sprite;
import summit.gui.HUD;
import summit.util.Controls;
import summit.util.Time;

public class PlayerEntity extends HumanoidEntity{

    private Camera camera;

    private HUD hud;

    private long lastAnimationChange = Time.timeMs();

    public PlayerEntity(float x, float y) {
        super(x, y, 1, 1);
        super.setDx(5f);
        super.setDy(5f);
        super.setHealth(10f);
        //DO THIS
        // super.setSprites(getName(), getName(), getName(), getName(), getName(), getName());
    }

    @Override
    public void paint(PaintEvent e) {
        
        e.getRenderer().renderGame(Sprite.PLAYER_FACE_BACK_1, 
                                    getX(), getY(), 
                                    ((Time.timeMs()-lastAnimationChange > 250) ? Renderer.FLIP_NONE : Renderer.FLIP_X) | 
                                    (inWater() ? Renderer.OUTLINE_RED | Renderer.OUTLINE_BLUE: Renderer.FLIP_NONE),
                                    e.getCamera());



        if((Time.timeMs()-lastAnimationChange > 500))
            lastAnimationChange = Time.timeMs();
    }

    @Override
    public void gameClick(GameMap map, MouseEvent e) {
        
    }

    @Override
    public void update(GameUpdateEvent e) {
        super.update(e);

        float del_x = (  getDx() / (inWater() ? 2 : 1) /Time.MS_IN_S)*e.getDeltaTime();
        float del_y = (  getDy() / (inWater() ? 2 : 1) /Time.MS_IN_S)*e.getDeltaTime();

        if(Controls.W && moveTo(e.getMap(), this.getX(), this.getY()+del_y)){
            camera.setY(this.getY()+del_y);
            this.setY(this.getY()+del_y);
        }
        if(Controls.A && moveTo(e.getMap(), this.getX()-del_x, this.getY())){
            camera.setX(getX()-del_x);
            this.setX(this.getX()-del_x);
        }
        if(Controls.S && moveTo(e.getMap(), this.getX(), this.getY()-del_y)){
            camera.setY(getY()-del_y);
            this.setY(this.getY()-del_y);
        }
        if(Controls.D && moveTo(e.getMap(), this.getX()+del_x, this.getY())){
            camera.setX(getX()+del_x);
            this.setX(this.getX()+del_x);
        }
    }

    @Override
    public void damage(GameUpdateEvent ge, Entity e) {
        
    }

    @Override
    public void destroy(GameUpdateEvent ge) {
        
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
