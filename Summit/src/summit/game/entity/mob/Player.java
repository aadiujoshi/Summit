/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.entity.mob;

import summit.deprecated.item.SnowballItem;
import summit.game.GameUpdateEvent;
import summit.game.animation.GlistenAnimation;
import summit.game.entity.projectile.Arrow;
import summit.gfx.Camera;
import summit.gfx.Light;
import summit.gfx.OrderPaintEvent;
import summit.gfx.PaintEvent;
import summit.gfx.Sprite;
import summit.gui.HUD;
import summit.util.Controls;
import summit.util.Region;
import summit.util.Sound;
import summit.util.Time;

public class Player extends HumanoidEntity{

    private Camera camera;

    private HUD hud;

    private boolean obtainedKeys[];
    
    public Player(float x, float y) {
        super(x, y, 1, 2);
        super.setDx(4.2f);
        super.setDy(4.2f);
        super.setHealth(10f);
        super.setMaxHealth(10f);
        super.setHitDamage(1);
        // super.setItems(new Inventory(this, 9, 5));
        super.collide(new SnowballItem(1, 1));
        super.setAI(null);
        
        // super.setLight(new Light(x, y, 5.5f, 100, 100, 100));

        this.hud = new HUD(this);

        this.obtainedKeys = new boolean[3];
        // this.invGui = new ItemGUI((Inventory)super.getItems());
    }
    
    @Override
    public void setRenderLayer(OrderPaintEvent e){
        super.setRenderLayer(e);
        hud.setRenderLayer(e);
    }

    @Override
    public void gameClick(GameUpdateEvent e) {
        // if(!invGui.isPushed()){
        //     e.getWindow().pushGameContainer(invGui);
        //     Controls.E = true;
        // }
    }

    @Override
    public void update(GameUpdateEvent e) {
        super.update(e);

        if(e.getMap().getName().equals("DungeonsMap"))
            super.setLight(new Light(getX(), getY(), 5.5f, 80, 80, 80));
        else
            super.setLight(Light.NO_LIGHT);
            
        //simulate click
        if(e.mouseClicked()){
            e.getMap().addAnimation(
                    new GlistenAnimation(
                        e.gameX(), e.gameY(), 
                        20, 
                        0x551a8b));

            e.getMap().spawn(new Arrow(this, 
                                Region.theta(e.gameX(), getX(), 
                                            e.gameY(), getY()),
                                            1));
        }

        // if(Controls.E){
        //     if(!invGui.isPushed())
        //         e.getWindow().pushGameContainer(invGui);
        //     return;
        // } else if(!Controls.E && invGui.isPushed()){
        //     e.getWindow().popGameContainer();
        // }

        float del_x = getDx()/Time.NS_IN_S *e.getDeltaTimeNS();
        float del_y = getDy()/Time.NS_IN_S *e.getDeltaTimeNS();

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

        //--- extra -----------------------------
        if(is(inWater))
            setSprite(Sprite.PLAYER_SUBMERGED_SOUTH);
        else if(!is(moving))
            setSprite(Sprite.PLAYER_NEUTRAL_SOUTH);
        else
            setSprite(Sprite.PLAYER_FACE_BACK_1);
        
        //-----------------------------------------
        //sounds

        if(is(moving) && !is(inWater)){
            if(!Sound.WALKING_HARD.playing())
                Sound.WALKING_HARD.play();
        } else{
            Sound.WALKING_HARD.stop();
        }
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
    
    public HUD getHud() {
        return this.hud;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
        setPos(camera.getX(), camera.getY());
    }

    /**
     * DO NOT USE THIS METHOD FOR RENDERING
     * @return This players camera
     */
    public Camera getCamera(){
        return this.camera;
    }

    public boolean[] getObtainedKeys(){
        return this.obtainedKeys;
    }

    // public ItemGUI getInventoryGui() {
    //     return this.invGui;
    // }

    // public void setInventoryGui(ItemGUI invGui) {
    //     this.invGui = invGui;
    // }
}
