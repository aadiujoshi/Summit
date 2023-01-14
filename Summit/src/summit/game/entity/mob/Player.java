package summit.game.entity.mob;

import summit.game.GameUpdateEvent;
import summit.game.GameWorld;
import summit.game.item.ArrowItem;
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
import summit.util.Controls;
import summit.util.ControlsReciever;
import summit.util.Region;
import summit.util.Sound;
import summit.util.Time;

/**
 * The Player class represents the playable character in the game. It extends
 * the HumanoidEntity class
 * and implements the ControlsReciever interface to allow the player to be
 * controlled by the user's input.
 * The Player class also has its own HUD, InventoryGUI, and equipped weapon
 * (either a Sword or Bow).
 * It has its own sprites for when it is in water and for its walking and
 * standing states, and it can
 * also pick up items.
 *
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R.
 */
public class Player extends HumanoidEntity implements ControlsReciever {

    private Camera camera;

    private HUD hud;
    private InventoryGUI invGui;

    private int xp;

    private Sword sword;
    private Bow bow;

    /**
     * Constructs a new Player at the specified x and y position.
     *
     * @param x the x position of the Player
     * @param y the y position of the Player
     */
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

        // addItems(new RedKey(this), 1);
        // addItems(new GreenKey(this), 1);
        // addItems(new BlueKey(this), 1);

        super.setEquipped(bow);

        Controls.addControlsReciever(this);
    }

    /**
     * Paints the Player, including a line from the Player to the mouse cursor if
     * the shift key is being held,
     * and the Player's attack range if the Player has a Sword equipped. The
     * Player's sprite is also set
     * based on whether it is in water or moving.
     *
     * @param e the PaintEvent to use for painting
     */
    @Override
    public void paint(PaintEvent e) {
        // draw line
        if (Controls.SHIFT) {
            if (getEquipped() != sword)
                e.getRenderer().drawLine(Renderer.WIDTH / 2,
                        Renderer.HEIGHT / 2,
                        e.mouseX(),
                        e.mouseY(),
                        (is(attackCooldown) ? 0xff0000 : 0x000000), true);
            if (getEquipped() == sword) {

                float theta = Region.theta(e.mouseX(), Renderer.WIDTH / 2, e.mouseY(), Renderer.HEIGHT / 2);

                float range = (float) (Math.PI / 6);

                // draw sword range
                int minx = (int) (getAttackRange() * 16 * Math.cos(theta - range)) + Renderer.WIDTH / 2;
                int miny = (int) (getAttackRange() * 16 * Math.sin(theta - range)) + Renderer.HEIGHT / 2;

                int maxx = (int) (getAttackRange() * 16 * Math.cos(theta + range)) + Renderer.WIDTH / 2;
                int maxy = (int) (getAttackRange() * 16 * Math.sin(theta + range)) + Renderer.HEIGHT / 2;

                e.getRenderer().drawLine(Renderer.WIDTH / 2,
                        Renderer.HEIGHT / 2,
                        minx,
                        miny,
                        (is(attackCooldown) ? 0xff0000 : 0x000000), true);

                e.getRenderer().drawLine(Renderer.WIDTH / 2,
                        Renderer.HEIGHT / 2,
                        maxx,
                        maxy,
                        (is(attackCooldown) ? 0xff0000 : 0x000000), true);
            }
        }

        // align with camera
        setSpriteOffsetX((e.getCamera().getX() - getX()));
        setSpriteOffsetY(0.5f + (e.getCamera().getY() - getY()));

        super.paint(e);
    }

    /**
     * 
     * Destroys the Player and removes its ControlReciever from the Controls list.
     * The player's items are also removed from the game world.
     */
    @Override
    public void destroy(GameUpdateEvent e) {
        super.destroy(e);
        e.getWorld().setCompletion(GameWorld.GAME_OVER_PLAYER_DEAD);
    }

    /**
     * 
     * Resets the state of the Player. This includes resetting the player's
     * position,
     * health, items, and equipped weapon.
     */
    @Override
    public void reinit() {
        super.reinit();
        sword.reinit();
        bow.reinit();
        Controls.addControlsReciever(this);
    }

    /**
     * 
     * Sets the render layer of the Player.
     * 
     * @param e the OrderPaintEvent to set the render layer to set the Player to
     */
    @Override
    public void setRenderLayer(OrderPaintEvent e) {
        super.setRenderLayer(e);
        hud.setRenderLayer(e);
    }

    /**
     * 
     * Updates the state of the Player, including its movement, attack, and item
     * usage. This method is called
     * every game tick.
     * 
     * @param e the GameUpdateEvent containing information about the current game
     *          tick
     */
    @Override
    public void update(GameUpdateEvent e) throws Exception {

        if (Controls.E) {
            if (!invGui.isPushed())
                e.getWindow().pushGameContainer(invGui);
            return;
        } else if (!Controls.E && invGui.isPushed()) {
            e.getWindow().popGameContainer();
        }

        super.update(e);

        if (getCurMap().equals("DungeonsMap") ||
                getCurMap().equals("BossRoom")) {
            Light li = new Light(getX(), getY(), 5.5f, 120, 120, 120);
            li.setRenderLayer(RenderLayers.STRUCTURE_ENTITY_LAYER + 1);
            super.setLight(li);
        } else
            super.setLight(Light.NO_LIGHT);

        // simulate click
        if (e.mouseClicked()) {
            if (Controls.SHIFT) {
                attack(e.gameX(), e.gameY(), e);
            }
        }

        float del_x = getDx() / Time.NS_IN_S * e.getDeltaTimeNS();
        float del_y = getDy() / Time.NS_IN_S * e.getDeltaTimeNS();

        if (Controls.W && moveTo(e.getMap(), this.getX(), this.getY() + del_y)) {
            this.setY(this.getY() + del_y);
        }
        if (Controls.A && moveTo(e.getMap(), this.getX() - del_x, this.getY())) {
            this.setX(this.getX() - del_x);
        }
        if (Controls.S && moveTo(e.getMap(), this.getX(), this.getY() - del_y)) {
            this.setY(this.getY() - del_y);
        }
        if (Controls.D && moveTo(e.getMap(), this.getX() + del_x, this.getY())) {
            this.setX(this.getX() + del_x);
        }

        if (is(moving) && !is(inWater)) {
            if (!Sound.WALKING_HARD.playing())
                Sound.WALKING_HARD.play();
        } else {
            Sound.WALKING_HARD.stop();
        }
    }

    /**
     * 
     * Handles a key press event for the Player. This includes
     * using items.
     *
     * @param e A static GameUpdateEvent
     */
    @Override
    public void keyPress(GameUpdateEvent e) {
        if (Controls.Q) {
            useItem(Sprite.APPLE_ITEM);
        }
        if (Controls.R) {
            setEquipped(bow);
        }
        if (Controls.C) {
            setEquipped(sword);
        }
        if (Controls.F) {
            if (getItems().countItem(Sprite.SNOWBALL) > 0)
                setEquipped((WeaponItem) getItems().get(Sprite.SNOWBALL).peek());
        }
        if (Controls.T) {
            if(countItems(Sprite.STICK_ITEM) > 0 && countItems(Sprite.BONE_ITEM) > 0){
                addItems(new ArrowItem(this), 1);
                useItem(Sprite.STICK_ITEM);
                useItem(Sprite.BONE_ITEM);
            }
        }
    }

    
    /** 
     * @param e
     */
    @Override
    public void keyRelease(GameUpdateEvent e) {

    }

    // ---------------- getters and setters --------------------------

    /**
     * 
     * Sets the x position of the Player.
     * 
     * @param x the x position to set
     */
    @Override
    public void setX(float x) {
        super.setX(x);
        camera.setX(x);
    }

    /**
     * 
     * Sets the y position of the Player.
     * 
     * @param y the y position to set
     */
    @Override
    public void setY(float y) {
        super.setY(y);
        camera.setY(y);
    }

    /**
     * 
     * Returns the HUD associated with the Player.
     * 
     * @return the HUD associated with the Player
     */
    public HUD getHud() {
        return this.hud;
    }

    /**
     * 
     * Sets the Camera associated with the Player.
     * 
     * @param camera the Camera to set
     */
    public void setCamera(Camera camera) {
        this.camera = camera;
        setPos(camera.getX(), camera.getY());
    }

    /**
     * 
     * Returns a boolean array indicating which keys the Player has obtained.
     * 
     * The indices of the array correspond to the red key (index 0), green key
     * (index 1),
     * 
     * and blue key (index 2). A value of true at an index indicates that the Player
     * 
     * has obtained the corresponding key.
     * 
     * @return a boolean array indicating which keys the Player has obtained
     */
    public boolean[] getObtainedKeys() {
        boolean[] keys = new boolean[3];

        ItemStorage is = getItems();

        if (is.countItem(Sprite.RED_KEY) > 0)
            keys[0] = true;
        if (is.countItem(Sprite.GREEN_KEY) > 0)
            keys[1] = true;
        if (is.countItem(Sprite.BLUE_KEY) > 0)
            keys[2] = true;

        return keys;
    }

    /**
     * 
     * Returns the Camera associated with the Player.
     * DO NOT USE THIS METHOD FOR RENDERING
     * 
     * @return the Camera associated with the Player
     */
    public Camera getCamera() {
        return this.camera;
    }

    /**
     * 
     * Returns the amount of experience points the Player has.
     * 
     * @return the amount of experience points the Player has
     */
    public int getXp() {
        return this.xp;
    }

    /**
     * 
     * Adds the specified amount of experience points to the Player.
     * 
     * @param exp the amount of experience points to add
     */
    public void addXp(int exp) {
        this.xp += exp;
    }

    // adds num copies of copy
    /**
     * 
     * Adds the specified number of copies of the given Item to the Player's
     * inventory.
     * 
     * A message is also displayed in the HUD indicating the number and type of Item
     * added.
     * 
     * @param copy the Item to add to the inventory
     * 
     * @param num  the number of copies of the Item to add
     */
    @Override
    public void addItems(Item copy, int num) {
        super.addItems(copy, num);

        if (num != 0)
            hud.addMessage("+" + (num + "") + " " + copy.getTextName());
    }

    /**
     * 
     * Adds all of the Items from the given ItemStorage to the Player's inventory.
     * 
     * A message is displayed in the HUD for each type of Item added, indicating the
     * number of that type of Item added.
     * 
     * @param items the ItemStorage containing the Items to add to the inventory
     */
    @Override
    public void pickupItems(ItemStorage items) {
        for (var i : items.entrySet()) {
            if ((i.getValue().size()) != 0 && i.getValue().peek() != null) {
                hud.addMessage("+" + (i.getValue().size()) + " " + i.getValue().peek().getTextName());
            }
        }

        super.pickupItems(items);
    }

    /**
     * 
     * Returns the reach of the player. This value represents the maximum distance
     * that the player can attack or interact with objects.
     * 
     * @return the reach of the player
     */
    public float getReach() {
        return 2.5f;
    }
}
