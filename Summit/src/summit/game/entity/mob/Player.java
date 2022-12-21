/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.entity.mob;

import java.util.HashMap;
import java.util.Stack;

import summit.game.GameUpdateEvent;
import summit.game.entity.projectile.Arrow;
import summit.game.item.BowItem;
import summit.game.item.Item;
import summit.game.item.Sword;
import summit.game.item.WeaponItem;
import summit.gfx.Camera;
import summit.gfx.Light;
import summit.gfx.OrderPaintEvent;
import summit.gfx.Sprite;
import summit.gui.HUD;
import summit.gui.InventoryGUI;
import summit.util.Controls;
import summit.util.Region;
import summit.util.Sound;
import summit.util.Time;

public class Player extends HumanoidEntity{
    
    private Camera camera;

    private HUD hud;
    private InventoryGUI invGui;

    private HashMap<String, WeaponItem> weapons;
    private WeaponItem equipped;

    private HashMap<String, Stack<Item>> items;

    private boolean obtainedKeys[];
    private int xp;
    
    public Player(float x, float y) {
        super(x, y, 1, 2);
        super.setDx(4.2f);
        super.setDy(4.2f);
        super.setMaxHealth(10f);
        super.setHealth(getMaxHealth());
        super.setAttackDamage(1);
        super.setAI(null);
        
        // super.setLight(new Light(x, y, 5.5f, 100, 100, 100));

        this.items = new HashMap<>();
        items.put(Sprite.ARROW_ITEM, new Stack<Item>());
        items.put(Sprite.SNOWBALL, new Stack<Item>());
        items.put(Sprite.APPLE_ITEM, new Stack<Item>());
        items.put(Sprite.STICK_ITEM, new Stack<Item>());
        items.put(Sprite.BONE_ITEM, new Stack<Item>());
        items.put(Sprite.GOLD_COIN, new Stack<Item>());

        this.weapons = new HashMap<>();
        weapons.put(Sprite.STONE_SWORD, new Sword(this));
        weapons.put(Sprite.BOW, new BowItem(this));

        this.hud = new HUD(this);
        this.invGui = new InventoryGUI(items);

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
        
        if(Controls.E){
            if(!invGui.isPushed())
                e.getWindow().pushGameContainer(invGui);
            return;
        } else if(!Controls.E && invGui.isPushed()){
            e.getWindow().popGameContainer();
        }


        super.update(e);

        if(!e.getMap().getName().equals("MainMap"))
            super.setLight(new Light(getX(), getY(), 5.5f, 80, 80, 80));
        else
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

    public int getXp() {
        return this.xp;
    }

    public void addXp(int exp) {
        this.xp += exp;
    }
    
    //adds num copies of base, simplename is used for the display message
    public void addItems(Item copy, String simpleName, int num){

        for(int i = 0; i < num; i++){
            items.get(copy.getSprite()).push((Item)copy.copy());
        }

        if(num != 0)
            hud.addMessage("+" + (num+"") + " " + simpleName);
    }
    
    // public ItemGUI getInventoryGui() {
    //     return this.invGui;
    // }

    // public void setInventoryGui(ItemGUI invGui) {
    //     this.invGui = invGui;
    // }
}
