package summit.game.entity.mob;

import summit.gfx.Camera;
import summit.gfx.PaintEvent;
import summit.gfx.Renderer;
import summit.util.Direction;
import summit.util.Time;

public abstract class HumanoidEntity extends MobEntity{

    private String sprite_north_moving;
    private String sprite_north_neutral;

    private String sprite_south_moving;
    private String sprite_south_neutral;

    private String sprite_east_moving;
    private String sprite_east_neutral;
    
    public HumanoidEntity(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    @Override
    public void paint(PaintEvent e){
        Direction d = getFacing();
        Camera c = e.getCamera();
        Renderer r = e.getRenderer();

        r.renderGame( switch(d){
                        case EAST -> (isMoving() ? sprite_east_moving : sprite_east_neutral);
                        case NORTH -> (isMoving() ? sprite_north_moving : sprite_north_neutral);
                        case SOUTH -> (isMoving() ? sprite_south_moving : sprite_south_neutral);
                        case WEST -> (isMoving() ? sprite_east_moving : sprite_east_neutral);
                        default -> null;
                    }, 
                    getX(), getY()+4, 
                    getRenderOp(), getColorFilter(),
                    c);

        // switch(d){
        //     case NORTH:
        //         if(isMoving()){
        //             r.renderGame(sprite_east_moving, getX(), getY(), r., c);
        //         }else{

        //         }
        //         break;

        //     case SOUTH:
        //         if(isMoving()){
        //             r.renderGame(sprite_east_moving, getX(), getY(), r., c);
        //         }else{

        //         }
        //         break;

        //     case EAST:
        //         if(isMoving()){
        //             r.renderGame(sprite_east_moving, getX(), getY(), r., c);
        //         }else{

        //         }
        //         break;

        //     case WEST:
        //         if(isMoving()){
        //             r.renderGame(sprite_east_moving, getX(), getY(), r., c);
        //         }else{

        //         }
        //         break;
        // }
    }

    protected void setSprites(String sprite_north_moving, String sprite_north_neutral, 
                                String sprite_south_moving, String sprite_south_neutral, 
                                String sprite_east_moving, String sprite_east_neutral){
        this.sprite_north_moving = sprite_north_moving;
        this.sprite_north_neutral = sprite_north_neutral;

        this.sprite_south_moving = sprite_south_moving;
        this.sprite_south_neutral = sprite_south_neutral;
        
        this.sprite_east_moving = sprite_east_moving;
        this.sprite_east_neutral = sprite_east_neutral;
    }
}