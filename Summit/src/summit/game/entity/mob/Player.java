/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.entity.mob;

import summit.game.GameUpdateEvent;
import summit.game.item.Bow;
import summit.game.item.Item;
import summit.game.item.ItemStorage;
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
        super.set(pickupItems, true);
        super.setAI(null);
        
        super.setSpriteStates(Sprite.PLAYER_SUBMERGED_SOUTH, 
                                Sprite.PLAYER_FACE_BACK_1, 
                                Sprite.PLAYER_NEUTRAL_SOUTH);

        this.hud = new HUD(this);
        this.invGui = new InventoryGUI(super.getItems());
        this.sword = new Sword(this);
        this.bow = new Bow(this);
        
        sword.setAttackRange(2f);

        super.setEquipped(bow);

        Controls.addControlsReciever(this);
    }
    
    @Override
    public void paint(PaintEvent e){
        //draw line
        if(Controls.SHIFT){
            if(getEquipped() != sword)
                e.getRenderer().drawLine(Renderer.WIDTH/2, 
                                    Renderer.HEIGHT/2, 
                                    e.mouseX(), 
                                    e.mouseY(), 
                                    (is(attackCooldown) ? 0xff0000 : 0x000000), true);
            if(getEquipped() == sword){
                
                float theta = Region.theta(e.mouseX(), Renderer.WIDTH/2, e.mouseY(), Renderer.HEIGHT/2);

                float range = (float)(Math.PI/6);

                //draw sword range
                int minx = (int)(getAttackRange()*16*Math.cos(theta - range)) + Renderer.WIDTH/2;
                int miny = (int)(getAttackRange()*16*Math.sin(theta - range)) + Renderer.HEIGHT/2;

                int maxx = (int)(getAttackRange()*16*Math.cos(theta + range)) + Renderer.WIDTH/2;
                int maxy = (int)(getAttackRange()*16*Math.sin(theta + range)) + Renderer.HEIGHT/2;

                e.getRenderer().drawLine(Renderer.WIDTH/2, 
                                        Renderer.HEIGHT/2, 
                                        minx, 
                                        miny, 
                                        (is(attackCooldown) ? 0xff0000 : 0x000000), true);
                                    
                e.getRenderer().drawLine(Renderer.WIDTH/2, 
                                        Renderer.HEIGHT/2, 
                                        maxx, 
                                        maxy, 
                                        (is(attackCooldown) ? 0xff0000 : 0x000000), true);
            }
        }

        //align with camera
        setSpriteOffsetX((e.getCamera().getX() - getX()));
        setSpriteOffsetY(0.5f+(e.getCamera().getY() - getY()));
        
        super.paint(e);
    }

    @Override
    public void reinit(){
        super.reinit();
        sword.reinit();
        bow.reinit();
        Controls.addControlsReciever(this);
    }

    @Override
    public void setRenderLayer(OrderPaintEvent e){
        super.setRenderLayer(e);
        hud.setRenderLayer(e);
    }

    @Override
    public void update(GameUpdateEvent e) throws Exception{
        
        if(Controls.E){
            if(!invGui.isPushed())
                e.getWindow().pushGameContainer(invGui);
            return;
        } else if(!Controls.E && invGui.isPushed()){
            e.getWindow().popGameContainer();
        }

        super.update(e);

        if(getCurMap().equals("DungeonsMap") || 
            getCurMap().equals("BossRoom")){
            Light li = new Light(getX(), getY(), 5.5f,120, 120, 120);
            li.setRenderLayer(RenderLayers.STRUCTURE_ENTITY_LAYER+1);
            super.setLight(li);
        } else
            super.setLight(Light.NO_LIGHT);
            
        //simulate click
        if(e.mouseClicked()){
            if(Controls.SHIFT){
                attack(e.gameX(), e.gameY(), e);
            }
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
        
        if(is(moving) && !is(inWater)){
            if(!Sound.WALKING_HARD.playing())
                Sound.WALKING_HARD.play();
        } else{
            Sound.WALKING_HARD.stop();
        }
    }

    @Override
    public void keyPress(GameUpdateEvent e) {
        if(Controls.Q){
            useItem(Sprite.APPLE_ITEM);
        }
        if(Controls.R){
            setEquipped(bow);
        }
        if(Controls.C){
            setEquipped(sword);
        }
        if(Controls.F){
            if(getItems().countItem(Sprite.SNOWBALL) > 0)
                setEquipped((WeaponItem)getItems().get(Sprite.SNOWBALL).peek());
        }
    }

    @Override
    public void keyRelease(GameUpdateEvent e) {
        
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
        
        ItemStorage is = getItems();

        if(is.countItem(Sprite.RED_KEY) > 0)
            keys[0] = true;
        if(is.countItem(Sprite.GREEN_KEY) > 0)
            keys[1] = true;
        if(is.countItem(Sprite.BLUE_KEY) > 0)
            keys[2] = true;
        
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

    public float getReach(){
        return 2.5f;
    }
}
