package summit.game.entity.mob;

import summit.game.GameClickEvent;
import summit.game.GameUpdateEvent;
import summit.game.animation.ScheduledEvent;
import summit.game.animation.Scheduler;
import summit.game.entity.Entity;
import summit.gfx.Camera;
import summit.gfx.ColorFilter;
import summit.gfx.Light;
import summit.gfx.OrderPaintEvent;
import summit.gfx.PaintEvent;
import summit.gfx.RenderLayers;
import summit.gfx.Renderer;
import summit.gfx.Sprite;
import summit.gui.HUD;
import summit.util.Controls;
import summit.util.Time;

public class Player extends HumanoidEntity{

    private Camera camera;

    private ScheduledEvent walkAnimation;

    private HUD hud;
    
    public Player(float x, float y) {
        super(x, y, 1, 1);
        super.setDx(6f);
        super.setDy(6f);
        super.setHealth(10f);
        super.setMaxHealth(10f);
        super.setSpriteOffsetX(0);
        super.setSpriteOffsetY(8);
        // super.setColorFilter(new ColorFilter(20, 100, -50));
        // var l = new Light(this.getX(), this.getY(), 1f, 170, 0, 0);
        // l.setShape(Light.Shape.SQUARE);
        // super.setLight(l);

        this.hud = new HUD();
        hud.setPlayer(this);
        this.walkAnimation = new ScheduledEvent(250, ScheduledEvent.FOREVER){

            boolean flipped = false;

            @Override
            public void run(){
                int op = getRenderOp();
                
                if(isMoving()){
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

        //DO THIS
        // super.setSprites(something, something, something, something, something, something);
        
    }


    @Override
    public void setRenderLayer(OrderPaintEvent ope) {
        super.setRenderLayer(ope);
        hud.setRenderLayer(ope);
    }

    @Override
    public void paint(PaintEvent e) {
        // super.paint(e);
        e.getRenderer().renderText(("x:" + Math.round(getX()*2)/2), 
                    20, 15, Renderer.NO_OP, new ColorFilter(-255, -255, -255));

        e.getRenderer().renderText(("y:" + Math.round(getY()*2)/2), 
                20, 25, Renderer.NO_OP, new ColorFilter(-255, -255, -255));

        e.getRenderer().renderGame(Sprite.PLAYER_FACE_BACK_1, 
                                        (getX()+(getSpriteOffsetX()/16f)), (getY()+(getSpriteOffsetY()/16f)), 
                                        getRenderOp(), getColorFilter(),
                                        e.getCamera());
    }

    @Override
    public void gameClick(GameClickEvent e) {
        System.out.println("clicked on player");
    }

    @Override
    public void update(GameUpdateEvent e) {
        
        super.update(e);

        float del_x = (  getDx() / (inWater() ? 2 : 1) /Time.NS_IN_S)*e.getDeltaTimeNS();
        float del_y = (  getDy() / (inWater() ? 2 : 1) /Time.NS_IN_S)*e.getDeltaTimeNS();

        if(Controls.W && moveTo(e.getMap(), this.getX(), this.getY()+del_y)){
            this.setY(this.getY()+del_y);
        }
        if(Controls.A && moveTo(e.getMap(), this.getX()-del_x, this.getY())){
            this.setX(this.getX()-del_x);
        }
        if(Controls.S && moveTo(e.getMap(), this.getX(), this.getY()-del_y)){
            this.setY(this.getY()-del_y);
        }
        if(Controls.D && moveTo(e.getMap(), this.getX()+del_x, this.getY())){
            this.setX(this.getX()+del_x);
        }
        
        this.updateIsMoving();
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
    
    //---------------- getters and setters --------------------------

    @Override 
    public void setX(float x){
        super.setX(x);
        camera.setX(x);
    }

    @Override
    public void setY(float y){
        super.setY(y);
        camera.setY(y);
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    /**
     * DO NOT USE THIS METHOD FOR RENDERING
     * @return This players camera
     */
    public Camera getCamera(){
        return this.camera;
    }
}
