/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.entity.mob;

import java.awt.Point;
import java.util.HashMap;
import java.util.Stack;

import summit.game.GameUpdateEvent;
import summit.game.entity.projectile.Arrow;
import summit.game.item.BlueKey;
import summit.game.item.Bow;
import summit.game.item.GreenKey;
import summit.game.item.Item;
import summit.game.item.ItemStorage;
import summit.game.item.RedKey;
import summit.game.item.Sword;
import summit.game.item.WeaponItem;
import summit.gfx.Camera;
import summit.gfx.Light;
import summit.gfx.OrderPaintEvent;
import summit.gfx.PaintEvent;
import summit.gfx.RenderLayers;
import summit.gfx.Renderer;
import summit.gfx.Sprite;
import summit.gui.HUD;
import summit.gui.InventoryGUI;
import summit.gui.Window;
import summit.util.Controls;
import summit.util.ControlsReciever;
import summit.util.Region;
import summit.util.Sound;
import summit.util.Time;

public class Player extends HumanoidEntity implements ControlsReciever{
    
    private Camera camera;

    private HUD hud;
    private InventoryGUI invGui;
    
    private int xp;

    private Sword sword;
    private Bow bow;
    
    public Player(float x, float y) {
        super(x, y);
        super.setDx(4.20f);
        super.setDy(4.20f);
        super.setMaxHealth(10f);
        super.setSpriteOffsetY(0.5f);
        super.setHealth(getMaxHealth());
        super.setAttackDamage(1);
        super.set(pickupItems, true);
        super.setAI(null);
        
        super.setEquipped(new Sword(this));
        
        super.addItems(new RedKey(this), 1);
        super.addItems(new GreenKey(this), 1);
        super.addItems(new BlueKey(this), 1);

        this.hud = new HUD(this);
        this.invGui = new InventoryGUI(super.getItems());
        this.sword = new Sword(this);
        this.bow = new Bow(this);

        Controls.addControlsReciever(this);
        
        // this.invGui = new ItemGUI((Inventory)super.getItems());
    }
    
    @Override
    public void paint(PaintEvent e){
        if(outline())
            this.outline(e);
        e.getRenderer().renderGame(getSprite(), 
                                    e.getCamera().getX()+getSpriteOffsetX(), e.getCamera().getY()+getSpriteOffsetY(), 
                                    getRenderOp(),
                                    getColorFilter(),
                                    e.getCamera());

        // Point p = Renderer.toPixel(getX(), getY(), e.getCamera());

        // e.getRenderer().fillRect((int)(1+p.x-getWidth()/2*16), (int)(p.y-getHeight()/2*16), (int)(getWidth()*16), (int)(getHeight()*16), Renderer.toIntRGB(0, 255, 0));
    }

    @Override
    public void reinit(){
        Controls.addControlsReciever(this);
    }

    @Override
    public void setRenderLayer(OrderPaintEvent e){
        super.setRenderLayer(e);
        hud.setRenderLayer(e);
    }

    @Override
    public void update(GameUpdateEvent e) {
        if(Controls.E){
            if(!invGui.isPushed())
                e.getWindow().pushGameContainer(invGui);
            return;
        } else if(!Controls.E && invGui.isPushed()){
            e.getWindow().popGameContainer();
        }

        super.update(e);

        if(!e.getMap().getName().equals("MainMap")){
            Light li = new Light(getX(), getY(), 5.5f,120, 120, 120);
            li.setRenderLayer(RenderLayers.STRUCTURE_ENTITY_LAYER+1);
            super.setLight(li);
        } else
            super.setLight(Light.NO_LIGHT);
            
        //simulate click
        if(e.mouseClicked()){
            e.getMap().spawn(new Arrow(this, 
                                Region.theta(e.gameX(), getX(), 
                                            e.gameY(), getY()),
                                            getProjDamage()));
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

    @Override
    public void keyPress() {
        if(Controls.Q){
            useItem(Sprite.APPLE_ITEM);
        }
        if(Controls.R){
            setEquipped(getEquipped());
        }

    }

    @Override
    public void keyRelease() {
        
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

    public boolean[] getObtainedKeys(){
        boolean[] keys = new boolean[3];
        
        for (var itemStack : getItems().entrySet()) {
            for (var it : itemStack.getValue()) {
                if(it.getTextName().equals("red key")){
                    keys[0] = true;
                }
                if(it.getTextName().equals("green key")){
                    keys[1] = true;
                }
                if(it.getTextName().equals("blue key")){
                    keys[2] = true;
                }
            }
        }

        return keys;
    }

    /**
     * DO NOT USE THIS METHOD FOR RENDERING
     * @return This players camera
     */
    public Camera getCamera(){
        return this.camera;
    }
    
    public int getXp() {
        return this.xp;
    }

    public void addXp(int exp) {
        this.xp += exp;
    }
    
    //adds num copies of copy
    @Override
    public void addItems(Item copy, int num){
        super.addItems(copy, num);

        if(num != 0)
            hud.addMessage("+" + (num+"") + " " + copy.getTextName());
    }

    @Override
    public void pickupItems(ItemStorage items){
        for (var i : items.entrySet()) {
            if((i.getValue().size()) != 0 && i.getValue().peek() != null){
                hud.addMessage("+" + (i.getValue().size()) + " " + i.getValue().peek().getTextName());
            }
        }

        super.pickupItems(items);
    }
}
