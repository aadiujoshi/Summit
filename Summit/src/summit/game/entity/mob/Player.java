/*
* BPA project by Aadi Joshi, Aditya Bhattaharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.entity.mob;

import summit.game.GameUpdateEvent;
import summit.game.entity.Entity;
import summit.game.entity.projectile.Snowball;
import summit.game.item.SnowballItem;
import summit.game.item.itemtable.Inventory;
import summit.gfx.Camera;
import summit.gfx.ColorFilter;
import summit.gfx.Light;
import summit.gfx.OrderPaintEvent;
import summit.gfx.PaintEvent;
import summit.gfx.Renderer;
import summit.gfx.Sprite;
import summit.gui.HUD;
import summit.gui.ItemGUI;
import summit.gui.Window;
import summit.util.Controls;
import summit.util.Time;

public class Player extends HumanoidEntity{

    private Camera camera;
    
    private HUD hud;
    private ItemGUI invGui;
    
    public Player(float x, float y, Camera mapCam) {
        super(x, y, 1, 2);
        super.setDx(6f);
        super.setDy(6f);
        super.setHealth(10f);
        super.setMaxHealth(10f);
        super.setHitDamage(1);
        super.setItems(new Inventory(this, 9, 5));
        super.collide(new SnowballItem(1, 1));
        super.setAI(null);
        super.setLight(new Light(x, y, 3, 110, 110, 0));

        this.hud = new HUD(this);
        this.invGui = new ItemGUI((Inventory)super.getItems());
        
        this.camera = mapCam;
        //DO THIS
        // super.setSprites(something, something, something, something, something, something);
    }

    @Override
    public void collide(Entity e){
        super.collide(e);
    }
    
    @Override
    public void setRenderLayer(OrderPaintEvent e){
        super.setRenderLayer(e);
        hud.setRenderLayer(e);
    }

    @Override
    public void paint(PaintEvent e) {
        super.paint(e);
        e.getRenderer().renderText(("x:" + (Math.round(getX()))), 
                    20, 15, Renderer.NO_OP, new ColorFilter(255, -255, -255));

        e.getRenderer().renderText(("y:" + (Math.round(getY()))), 
                20, 25, Renderer.NO_OP, new ColorFilter(255, -255, -255));

        e.getRenderer().renderGame(Sprite.PLAYER_FACE_BACK_1, 
                                        (getX()+(getSpriteOffsetX()/16f)), (getY()+(getSpriteOffsetY()/16f)), 
                                        getRenderOp(), getColorFilter(),
                                        e.getCamera());                 
    } 

    @Override
    public void gameClick(GameUpdateEvent e) {
        if(!invGui.isPushed()){
            e.getWindow().pushGameContainer(invGui);
            Controls.E = true;
        }
    }

    @Override
    public void update(GameUpdateEvent e) {
        super.update(e);
        
        if(Window.mouseDown()){
            e.getMap().spawn(new Snowball(getX(), getY(), getHeight()));
        }

        if(Controls.E){
            if(!invGui.isPushed())
                e.getWindow().pushGameContainer(invGui);
            return;
        } else if(!Controls.E){
            e.getWindow().popGameContainer();
        }

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

    public ItemGUI getInventoryGui() {
        return this.invGui;
    }

    public void setInventoryGui(ItemGUI invGui) {
        this.invGui = invGui;
    }
}
