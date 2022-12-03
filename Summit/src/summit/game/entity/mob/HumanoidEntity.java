/*
* BPA project by Aadi Joshi, Aditya Bhattaharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.entity.mob;

import summit.game.GameUpdateEvent;
import summit.gfx.Camera;
import summit.gfx.PaintEvent;
import summit.gfx.Renderer;
import summit.util.Direction;
import summit.util.ScheduledEvent;
import summit.util.Scheduler;

public abstract class HumanoidEntity extends MobEntity{

    private String sprite_north_moving;
    private String sprite_north_neutral;

    private String sprite_south_moving;
    private String sprite_south_neutral;

    private String sprite_east_moving;
    private String sprite_east_neutral;
    
    private ScheduledEvent walkAnimation;

    public HumanoidEntity(float x, float y, float width, float height) {
        super(x, y, width, height);

        this.walkAnimation = new ScheduledEvent(250, ScheduledEvent.FOREVER){

            boolean flipped = false;

            @Override
            public void run(){
                int op = getRenderOp();
                
                if(is(moving)){
                    if(flipped)
                        setRenderOp(op ^ Renderer.FLIP_X);
                    else
                        setRenderOp(op | Renderer.FLIP_X);
                } else{
                    setRenderOp(op & ~Renderer.FLIP_X);
                }

                flipped = !flipped;
            }
        };

        Scheduler.registerEvent(walkAnimation);
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

    @Override
    public void reinit(){
        Scheduler.registerEvent(this.walkAnimation);
    }

    @Override
    public void gameClick(GameUpdateEvent e) {
        damage(e.getMap().getPlayer());
    }

    // @Override
    public void paintNOTDONE(PaintEvent e){
        super.paint(e);
        
        Direction d = getFacing();
        Camera c = e.getCamera();
        Renderer r = e.getRenderer();

        r.renderGame( switch(d){
                        case EAST -> (is(moving) ? sprite_east_moving : sprite_east_neutral);
                        case NORTH -> (is(moving) ? sprite_north_moving : sprite_north_neutral);
                        case SOUTH -> (is(moving) ? sprite_south_moving : sprite_south_neutral);
                        case WEST -> (is(moving) ? sprite_east_moving : sprite_east_neutral);
                        default -> null;
                    }, 
                    getX(), getY()+4, 
                    Renderer.NO_OP, getColorFilter(),
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
}
