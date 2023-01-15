
package summit.game.entity.mob;

import summit.gfx.PaintEvent;
import summit.gfx.Renderer;
import summit.util.GraphicsScheduler;
import summit.util.ScheduledEvent;

/**
 * 
 * HumanoidEntity is an abstract class that extends MobEntity to provide
 * additional features and behaviors specific to humanoid entities.
 * 
 * It has a ScheduledEvent called walkAnimation that flips the sprite
 * horizontally every 400 milliseconds while the HumanoidEntity is moving.
 * 
 * The paint method of HumanoidEntity will set the sprite to the appropriate
 * state based on whether the HumanoidEntity is in water, moving, or neither.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R. S.
 */
public abstract class HumanoidEntity extends MobEntity {

    // private String sprite_north_moving;
    // private String sprite_north_neutral;

    // private String sprite_south_moving;
    // private String sprite_south_neutral;

    // private String sprite_east_moving;
    // private String sprite_east_neutral;

    private String sprite_submerged;
    private String sprite_walking;
    private String sprite_neutral;

    /**
     * 
     * A ScheduledEvent that flips the sprite horizontally every 400 milliseconds
     * while the HumanoidEntity is moving.
     */
    private ScheduledEvent walkAnimation;

    /**
     * 
     * Constructs a new HumanoidEntity with the specified x and y position.
     * 
     * @param x the x position of the HumanoidEntity
     * 
     * @param y the y position of the HumanoidEntity
     */
    public HumanoidEntity(float x, float y) {
        super(x, y, 1, 1);
        super.setSpriteOffsetY(0.5f);

        this.walkAnimation = new ScheduledEvent(400, ScheduledEvent.FOREVER) {

            boolean flipped = false;

            @Override
            public void run() {
                int op = getRenderOp();

                if (is(moving)) {
                    if (flipped)
                        setRenderOp(op ^ Renderer.FLIP_X);
                    else
                        setRenderOp(op | Renderer.FLIP_X);
                } else {
                    setRenderOp(op & ~Renderer.FLIP_X);
                }

                flipped = !flipped;
            }
        };

        GraphicsScheduler.registerEvent(walkAnimation);
    }

    /**
     * 
     * Sets the three sprite states for this HumanoidEntity.
     * 
     * @param sprite_submerged the sprite to use when the HumanoidEntity is in water
     * 
     * @param sprite_walking   the sprite to use when the HumanoidEntity is moving
     * 
     * @param sprite_neutral   the sprite to use when the HumanoidEntity is not
     *                         moving and not in water
     */
    protected void setSpriteStates(String sprite_submerged,
            String sprite_walking,
            String sprite_neutral) {

        this.sprite_neutral = sprite_neutral;
        this.sprite_submerged = sprite_submerged;
        this.sprite_walking = sprite_walking;
    }

    /**
     * Sets the sprite of this HumanoidEntity to the appropriate state based on
     * whether the HumanoidEntity is in water,
     * moving, or neither, and then calls the paint method of the superclass.
     * 
     * @param e the PaintEvent to use for painting
     */
    @Override
    public void paint(PaintEvent e) {
        if (is(inWater))
            setSprite(sprite_submerged);
        else if (!is(moving))
            setSprite(sprite_neutral);
        else
            setSprite(sprite_walking);

        super.paint(e);
    }

    // protected void setSprites(String sprite_north_moving, String
    // sprite_north_neutral,
    // String sprite_south_moving, String sprite_south_neutral,
    // String sprite_east_moving, String sprite_east_neutral){
    // this.sprite_north_moving = sprite_north_moving;
    // this.sprite_north_neutral = sprite_north_neutral;

    // this.sprite_south_moving = sprite_south_moving;
    // this.sprite_south_neutral = sprite_south_neutral;

    // this.sprite_east_moving = sprite_east_moving;
    // this.sprite_east_neutral = sprite_east_neutral;
    // }

    /**
     * Calls the reinit method of the superclass and registers the walkAnimation
     * ScheduledEvent with the
     * GraphicsScheduler.
     */
    @Override
    public void reinit() {
        super.reinit();
        GraphicsScheduler.registerEvent(this.walkAnimation);
    }

    
    /** 
     * @param e
     */
    // @Override
    public void paintNOTDONE(PaintEvent e) {

        // Direction d = getFacing();
        // Camera c = e.getCamera();
        // Renderer r = e.getRenderer();

        // r.renderGame( switch(d){
        // case EAST -> (is(moving) ? sprite_east_moving : sprite_east_neutral);
        // case NORTH -> (is(moving) ? sprite_north_moving : sprite_north_neutral);
        // case SOUTH -> (is(moving) ? sprite_south_moving : sprite_south_neutral);
        // case WEST -> (is(moving) ? sprite_east_moving : sprite_east_neutral);
        // default -> null;
        // },
        // getX(), getY()+4,
        // Renderer.NO_OP, getColorFilter(),
        // c);

        // switch(d){
        // case NORTH:
        // if(isMoving()){
        // r.renderGame(sprite_east_moving, getX(), getY(), r., c);
        // }else{

        // }
        // break;

        // case SOUTH:
        // if(isMoving()){
        // r.renderGame(sprite_east_moving, getX(), getY(), r., c);
        // }else{

        // }
        // break;

        // case EAST:
        // if(isMoving()){
        // r.renderGame(sprite_east_moving, getX(), getY(), r., c);
        // }else{

        // }
        // break;

        // case WEST:
        // if(isMoving()){
        // r.renderGame(sprite_east_moving, getX(), getY(), r., c);
        // }else{

        // }
        // break;
        // }
    }
}
