package summit.game.entity.mob;

import java.awt.event.MouseEvent;

import summit.game.GameMap;
import summit.game.GameUpdateEvent;
import summit.game.animation.ScheduledEvent;
import summit.game.entity.Entity;
import summit.gfx.Camera;
import summit.gfx.ColorFilter;
import summit.gfx.PaintEvent;
import summit.gfx.Renderer;
import summit.gfx.Sprite;
import summit.gui.HUD;
import summit.util.Controls;
import summit.util.Time;

public class PlayerEntity extends HumanoidEntity{

    private Camera camera;

    private HUD hud;
    
    public PlayerEntity(float x, float y) {
        super(x, y, 1, 1);
        super.setDx(5f);
        super.setDy(5f);
        super.setHealth(10f);
        super.setMaxHealth(10f);
        super.setColorFilter(new ColorFilter(100, 0, 0));
        this.hud = new HUD();
        hud.setPlayer(this);
        //DO THIS
        // super.setSprites(something, something, something, something, something, something);
        
    }

    @Override
    public void paint(PaintEvent e) {
        
        e.renderLater(hud);
        
        // System.out.println(isMoving());

        if(isMoving()){
            e.getRenderer().renderGame(Sprite.PLAYER_FACE_BACK_1, 
                                        getX(), getY(), 
                                        getRenderOp(), getColorFilter(),
                                        e.getCamera());
        
        } else {
            e.getRenderer().renderGame(Sprite.PLAYER_FACE_BACK_1, 
                                        getX(), getY(), 
                                        getRenderOp(), getColorFilter(),
                                        e.getCamera());
        }
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
